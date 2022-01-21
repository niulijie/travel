package com.niulijie.springboot.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.niulijie.common.constant.CommonConstant;
import com.niulijie.common.dto.ResultStatus;
import com.niulijie.common.exception.CustomException;
import com.niulijie.common.utils.QRCodeUtil;
import com.niulijie.springboot.dao.PostQPCodeResponse;
import com.niulijie.springboot.entity.DaPrLocation;
import com.niulijie.springboot.entity.DaPrLocationDict;
import com.niulijie.springboot.entity.DaPrPost;
import com.niulijie.springboot.mapper.DaPrLocationDictMapper;
import com.niulijie.springboot.mapper.DaPrLocationMapper;
import com.niulijie.springboot.mapper.DaPrPostMapper;
import com.niulijie.springboot.service.PoliceReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 警力报备处理逻辑
 * @author df
 * @date 2021/12/25
 */
@Slf4j
@Service
public class PoliceReportServiceImpl implements PoliceReportService {

    @Resource
    private DaPrLocationMapper daPrLocationMapper;

    @Resource
    private DaPrPostMapper daPrPostMapper;

    @Resource
    private DaPrLocationDictMapper daPrLocationDictMapper;

    /**
     * 拼接岗位名称
     * @param daPrPosts
     * @return
     */
    private List<DaPrPost> getOtherLevelInfo(List<DaPrPost> daPrPosts){
        List<DaPrPost> daPrPostList = new ArrayList<>();
        //将所有数据将场馆编号编号分组
        Map<String, List<DaPrPost>> daPrPostMap = daPrPosts.stream().collect(Collectors.groupingBy(DaPrPost::getLocNum));
        for (Map.Entry<String, List<DaPrPost>> map :daPrPostMap.entrySet()){
            //找到该组场馆的第一级
            Integer minLevel = map.getValue().stream().map(DaPrPost::getPostLevel).min(Integer::compare).get();
            //从一级开始找，判断是否有下一级，如果有则拼接名字
            map.getValue().stream().filter(daPrPost -> daPrPost.getPostLevel().equals(minLevel)).forEach(oneLevelDaPrPost -> {
                this.geneName(map.getValue(), minLevel, oneLevelDaPrPost, daPrPostList);
            });
        }
        return daPrPostList;
    }

    /**
     * 判断是否有下一级,如果有下一级，拼接postName参数
     * @param daPrPosts
     * @param minLevel
     * @param daPrPost
     * @param daPrPostList
     */
    private void geneName(List<DaPrPost> daPrPosts, Integer minLevel, DaPrPost daPrPost, List<DaPrPost> daPrPostList) {
        AtomicReference<Integer> finalMinLevel = new AtomicReference<>(minLevel);
        //查询下一级
        List<DaPrPost> nextLevelList = daPrPosts.stream().filter(daPrPostTemp -> daPrPostTemp.getPostLevel()
                .equals(daPrPost.getPostLevel() + 1)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(nextLevelList)) {
            List<String> postCodes = nextLevelList.stream()
                    .map(daPrPostNext -> daPrPostNext.getPostCode().substring(0, daPrPostNext.getPostCode().length() - 3))
                    .distinct().collect(Collectors.toList());
            //如果存在下一级，需要再查找下一级
            if (postCodes.contains(daPrPost.getPostCode())) {
                for (DaPrPost daPrPostNext : nextLevelList) {
                    //找到下一级，并且拼接名字
                    if (Objects.equals(daPrPost.getPostCode(), daPrPostNext.getPostCode().substring(0, daPrPostNext.getPostCode().length() - 3))) {
                        daPrPostNext.setPostName(daPrPost.getPostName() + "-" + daPrPostNext.getPostName());
                        geneName(daPrPosts, finalMinLevel.get(), daPrPostNext, daPrPostList);
                    }
                }
            } else {
                //不存在下一级，则本节点就是所需数据
                daPrPostList.add(daPrPost);
            }
        } else {
            //不存在下一级，则本节点就是所需数据
            daPrPostList.add(daPrPost);
        }
    }

    /**
     * 获取岗位列表用于生成二维码并转换成base64存储
     * @param daPrPosts
     */
    /**
     * 获取岗位列表用于生成二维码并转换成base64存储
     * @param daPrPosts
     */
    @Override
    public List<DaPrPost> generatePostQPCodeWith64(List<DaPrPost> daPrPosts){
        //拼接名字
        daPrPosts = this.getOtherLevelInfo(daPrPosts);
        //场馆类型名称列表
        List<DaPrLocation> daPrLocations = daPrLocationMapper.selectList(Wrappers.<DaPrLocation>lambdaQuery()
                .select(DaPrLocation::getLocTypeCode, DaPrLocation::getLocNum, DaPrLocation::getLocName));
        if(CollectionUtils.isEmpty(daPrLocations)){
            throw new CustomException(ResultStatus.http_status_bad_request.getErrorCode(),"未找到场馆信息");
        }
        Map<String, String> LocationNameMap = daPrLocations.stream().collect(Collectors.
                toMap(DaPrLocation::getLocNum, DaPrLocation::getLocName));

        Map<String, String> LocationDictNameMap = daPrLocations.stream().collect(Collectors.
                toMap(DaPrLocation::getLocNum, DaPrLocation::getLocTypeCode));

        //场馆类型名称列表
        List<DaPrLocationDict> daPrLocationDicts = daPrLocationDictMapper.selectList(Wrappers.<DaPrLocationDict>lambdaQuery()
                .select(DaPrLocationDict::getLocTypeCode, DaPrLocationDict::getLocTypeName));
        if(CollectionUtils.isEmpty(daPrLocationDicts)){
            throw new CustomException(ResultStatus.http_status_bad_request.getErrorCode(),"未找到场馆类别信息");
        }
        Map<String, String> LocationDictMap = daPrLocationDicts.stream().collect(Collectors.
                toMap(DaPrLocationDict::getLocTypeCode, DaPrLocationDict::getLocTypeName));
        try {
            for (DaPrPost daPrPost : daPrPosts) {
                PostQPCodeResponse postQPCodeResponse = new PostQPCodeResponse();
                postQPCodeResponse.setPostStatus(daPrPost.getPostStatus());
                postQPCodeResponse.setPostCode(daPrPost.getPostCode());
                postQPCodeResponse.setLocNum(daPrPost.getLocNum());
                if (Objects.equals(daPrPost.getPostCircle(), CommonConstant.POST_CIRCLE_INNER)) {
                    postQPCodeResponse.setPostName(URLEncoder.encode("闭环内-" + daPrPost.getPostFullName(), "UTF-8"));
                } else if (Objects.equals(daPrPost.getPostCircle(), CommonConstant.POST_CIRCLE_OUTER)) {
                    postQPCodeResponse.setPostName(URLEncoder.encode("闭环外-" + daPrPost.getPostFullName(), "UTF-8"));
                } else {
                    postQPCodeResponse.setPostName(URLEncoder.encode(daPrPost.getPostFullName(), "UTF-8"));
                }
                postQPCodeResponse.setLocTypeCode(LocationDictNameMap.get(daPrPost.getLocNum()));
                postQPCodeResponse.setLocName(URLEncoder.encode(LocationNameMap.get(daPrPost.getLocNum()), "UTF-8"));
                postQPCodeResponse.setLocTypeName(URLEncoder.encode(LocationDictMap.get(postQPCodeResponse.getLocTypeCode()), "UTF-8"));
                //生成二维码，并且将二维码图片转成base64格式，以岗位主键作为二维码图片名称
                String fileName = postQPCodeResponse.getLocTypeCode() + "-" + daPrPost.getLocNum()  + "-" + daPrPost.getPostCode();
                String encode = QRCodeUtil.encode(Base64.getEncoder().encodeToString(new Gson().toJson(postQPCodeResponse).getBytes()), "", fileName, true);
                daPrPost.setCodeImg(encode);

            }
        } catch (Exception e) {
            log.info("岗位列表生成二维码并转换成base64存储失败："+e.getMessage());
            e.printStackTrace();
        }
        return daPrPosts;
    }

    @Override
    public void generatePostQPCode() throws IOException, WriterException {
        String text = "场馆内-竞赛场馆-首都体育馆-场院巡控-南区赛事中心地下停车场三层巡控(闭环内)";
        String encode = URLEncoder.encode(text, "utf-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(encode, BarcodeFormat.QR_CODE, 360, 360);

        Path path = FileSystems.getDefault().getPath("D:/项目资料/MyQRCode.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] dataArray = pngOutputStream.toByteArray();

        OutputStream out = new FileOutputStream("D:/项目资料/MyQRCode-temp.png");
        InputStream is = new ByteArrayInputStream(dataArray);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len=is.read(buff))!=-1){
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }

    /**
     * 文件读取方式
     */
    public void testDecode1(){
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            fileReader = new FileReader("C:\\Users\\niuli\\Desktop\\接口.txt");
            bufferedReader = new BufferedReader(fileReader);
            String tempStr;
            while ((tempStr = bufferedReader.readLine()) != null){
                sbf.append(tempStr);
            }
            bufferedReader.close();
            System.out.println("文件中内容为："+sbf);
            String decode1 = URLDecoder.decode(sbf.toString(), "UTF-8");
            System.out.println(decode1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
    public void testDecode2() throws IOException {

        System.out.println(Charset.defaultCharset());
        BufferedReader bufferedReader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\niuli\\Desktop\\接口.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String tempStr;
            while ((tempStr = bufferedReader.readLine()) != null){
                sbf.append(tempStr);
            }
            bufferedReader.close();
            System.out.println("文件中内容为："+sbf);
            String decode1 = URLDecoder.decode(sbf.toString(), "UTF-8");
            System.out.println(decode1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testDecode() throws Exception {
        String decode = QRCodeUtil.decode("D:/项目资料/MyQRCode.png");
        String decode1 = URLDecoder.decode(decode, "UTF-8");
        System.out.println(decode1);

        String decode2 = QRCodeUtil.decode("D:/项目资料/MyQRCode-temp.png");
        String decodeStr = URLDecoder.decode(decode2, "UTF-8");
        System.out.println(decodeStr);
    }
}

