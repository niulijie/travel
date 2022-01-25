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
        //直接写文件
        Path path = FileSystems.getDefault().getPath("D:/项目资料/MyQRCode.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        //将内容自己写成输出流pngOutputStream
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] dataArray = pngOutputStream.toByteArray();

        //输出流pngOutputStream再转换成文件，写入到D:/项目资料/MyQRCode-temp.png中
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
     * 文件读取方式1
     */
    private void testDecode1(){
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

    /**
     * 文件读取方式2
     */
    private void testDecode2() throws IOException {
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

    @Override
    public void testDecode3() throws UnsupportedEncodingException {
        String code = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAyAAAAMgCAIAAABUEpE/AAA0+ElEQVR42u3YUYLcOg4DwLn/pd8e\n" +
                "YtMEIBe+nbQtUWRp/v4TERERkX+aP0sgIiIiAlgiIiIigCUiIiICWCIiIiICWCIiIiKAJSIiIgJY\n" +
                "IiIiIgJYIiIiIoAlIiIiAlgiIiIiAlgiIiIigCUiIiICWCIiIiKAJSIiIiKAJSIiIgJYIiIiIoAl\n" +
                "IiIiIofA+uvL3ZJFX/69BTl7pezLZ1/pF7/+f/5Q4a9nly57YLNPrnSws5ovXLqVBlilC8ACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACrJ83spWdLlylFbgU0jY7kr+swywuC/d9pdNOc2T6+jGtruwq\n" +
                "pU4cYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAFW0QxYcc+ZElbmykcG7VlzLMTldBNfEXxWCVmf\n" +
                "FZ64Qoplf6hwkVO8AyzAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAenYJjnvB9Ph07Kt4l12l6d2c\n" +
                "Xs/Cy2R2pp69Z+GVO1sM0yMSsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsGaezE617MZlV6nQ\n" +
                "5YU+W2FTdpFXtrhQHu+d9yxtp3cTsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsMLVk63IXwyb\n" +
                "7MtPj5DC2V9YdYXfXjj/zo52ITKyfWnaPdMXv8L2O6oLwAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAKsTC84a+Ke9KQnPelJT3qy8MlCXQCWMvWkJz3pSU96ErAAC7A86UlPetKTnvQkYAGWJz3pSU96\n" +
                "0pOeBCzA8qQnPelJT3rSk4AFWIDlSU960pOe9CRgARZgedKTnvSkJz3pScB6NtntP3vPs1fKHryz\n" +
                "lz/7PwsL7OwYZpfuF+U9XSGFnbawvKf3/awBZkuxSyCABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViLo+6sjU63+8LteO+LCi8AhQRfWfnpCpk2ynRjKVyllT0qvDcCFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmABFmD1Aqt/BY8/M7ueK595NldWukb2ppHFZZYjhSN5ulV++chke930RSW1R4AFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWD8fNtk5Xbh/f9EUdvbsZ06zPuvI6e2YviatyGN6iwsNPT1MX2os\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYk02nsM5W4PKRCTRdioWteUVyK+eoUF3ZBpjtS++B\n" +
                "IKvD56/cgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY7Vs1XaaFPitsjtnZ/14tZYd39j1X7jnT\n" +
                "TXX6opIl40rVZcdE/yIDFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAVrUt2rT/iyL8f5L1jX6iZ\n" +
                "lZovFPzZk9ma//IWF+779P95NjtW7mOABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABVi9IDibK2cl\n" +
                "lR0M00d0pbdmD0IW6ys6LGT99HlfOR1n5Z0tm8Ih9XwxABZgARZgARZgARZgARZgARZgARZgARZg\n" +
                "ARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZg\n" +
                "ARZgOczXL5/9osKczdSVjrli/S/b9O8beY/gH0HGe38rqVIXYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAFWZquyfTC7ICuNrJAjhTL+yExd2Y6Vo332Re/5LEuHL9/DCykW+6MAYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAFWZAU/8s9XRkghsM7Wc1rGhTei9w7X2fz7yCK/dzan77crxKkqBsACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACrJ9z5Kwmsofk7Nvf88RKvz5bpbP6LDza2RlQeLQLLbVCW9fO\n" +
                "qlOc/T//7Q8BFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmBNrmDhsPkyGQvB+uWNK2xkhetZuB0r\n" +
                "h6uwlgrVNX1kVhpgajcBy5wGLMACLMACLMACLMACLMCycYAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WOY0YAEWYAEWYAEWYAEWYAGWjQMswAIswAIswAIswAIswAIswAIswAKszFpn+0vht2crctp804P2\n" +
                "7GwW1lJ2pp7dGws/86yWqoZibZ//8qV35ZUAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AqUljQ\n" +
                "07N/+pUKR8hKG81OoPcQvHLDzM5Uzarqyp39c8P0NQmwAEvPAizAAizAAizAAizAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizA0qwA\n" +
                "C7D+DbAKO/u05N5j6FkXXuHdyrRYuWkUIqOwfxYuyHvXpMLZ7zpXsh2ABViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABVhFO11IsRWfFZ6HlREyrW0Nlw7vh8102axcO1cmQmHVARZgARZgARZgARZgARZg\n" +
                "ARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZg\n" +
                "ARZgARZgARZgARZgVQBrpXY/gozCKi/sWYUXgGymL1T9DXdrKJ7t5nu31pXDtbJxhVUHWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWID1w5Ka3umzWbXSXwrBmv3MlZZXKOPClT8btNO0nb7nfOTS+96C\n" +
                "jE4uwAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAKsL9bESnvKInjFEys/tNLEC897tubfu7mtFEOh\n" +
                "dz/Sggq/HbAAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAawxY9q8KLoUcWTHfWSn+ourOnvzI/Ms2\n" +
                "q+wWT5/3Qu9m63P6dp1t1IAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWD8E1soRzVbk2ahbaTr2\n" +
                "ffT+UDhos4otvCK+N1MLt3jllc7Ke/p2DViABVheCbAAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7Bo\n" +
                "BrAAC7AAC7AAC7AAC7C8EmABFmABFmABlkELWPYdsAALsAALsAALsACLZgALsAALsAALsH4IrBVP\n" +
                "nDXHwqFY+JkHBV37SisXlff2ffr/LJwr0+c9K6SP7PvKObro24AFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWJGKzD5Z2J7eY1Phbr7XhVcKLNtGsx1s2tDZkZy9KqwMlJXDVbjvgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYFcA6a/crLS/bMVf2aAUu2e3IVt1Zc/wyxQqRsbLv2bvoShPIfmahjAEL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsP4NsLLndqWksn2wcFa9t+9f/vaPQHCa9dM+K7ygFoKg\n" +
                "sIOt6PBixwELsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsJ7Zv7PtL2yjhY48K/1CMmbfM0uHs91c\n" +
                "uagUjvkVYK0MqcL+mTVfoQQAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7B+CKyznS7sWdkzttLI\n" +
                "sv3FtDh4pcJTXKjYlXaR1fbKK2W3uHDsrlRd7DIDWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAV\n" +
                "qYnCfn3WSgpbc7bdT6sra/0VJRRiqPBWMG3o6ZUv7HXTE3alKwIWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYF0DKwuC7Mk5OySF0zd7mM82rnAo/kVTuEorPlsZIe+tUrbXfVkz0xUCWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIA1VruFTSerhEIdZpvOR0AwvfLvQfBsPQt9lv2iwnbx3jScvnqlaAtY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgGXlAQuwAAuwAAuwAAuwAAuwAAuwAAuwAAuw\n" +
                "AAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAOvnNZFaggYMFc6qQvOdzZXpfp2daoVHu/DErVznsouc\n" +
                "PYYrNb9Sn2cur/pMwAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAKszMl5r+GukHFl9md9tlJg2T5Y\n" +
                "iMvpy89ZhZzdnbJNVfdO3ZnfW2TAAizAAizAAizAAizAAizAAizAAizAAizAAizAAixHFLAAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAS/cGLMACLMACLMACLMACLMACLMACLMACLMACLMACLMD64cGbHvPZMj0z\n" +
                "SqoiH3il7BcV+mzlM7PDJquE6fNe2C6mu81KLRW2NcACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "rIrzMF27Z4P27yrTTWels2efPCNONoXmW/FZ4fT9cjGYCM1cBizAAizAAizAAizAMhEAC7AcJ8AC\n" +
                "LMACLMAyEQALsAALsAALsAALsAALsAALsAALsAALsAALsAALsADLcQIswAIswAIsEwGwAAuwAAuw\n" +
                "AAuwAAuwAKudOIXuKfz2lYa7MtGzksvu+0euH9m+VNiTs0d7+pU+0m1WhHTwmYAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWO8UdLaz99dEbRfOKnblhGcHQ/YgZPeosINlQZClQ2HZrJTie/UJWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIDVC6yz0j9DRmFvPYPg2fjM/lAWgoXWX9mO/t66dU2avhFl\n" +
                "W/p0B1vh8soPARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZg\n" +
                "ARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgXR+n6eKr2tTaVpLV4dkiF/rsvda8\n" +
                "siAryCjsISusL6zP6T92FH4mYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAHWvwFWYb+erp4zzazM\n" +
                "v/eOfaFis7tZmOnh/V7VTR/YwvvDivkKXQ5YgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgPVDYBUe\n" +
                "+xV1FX5RtnZXzLcyfbNn872qK6RttlFnW9D0He+9tlb4B5TUygMWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYH2x5U0bpbD4sv36PTYV1nzhrxeO+em5clYhK4otNPTKVeF5wQMWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYGU29azhZn9o5duraveYtn8jKcRQ4W4Wzqrp+Zd9pem+9N48Wrk7HVQIYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAHWFxvZ9ETPDtpp2mbL5qwXrPTWlZfP4vI9xRa23+zhWvn2\n" +
                "QusDFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmABFmABFmABFmABFmABFmABFmABFmABFmBVACu7rIVVPt0cp4VUuO8ryQ7FFXFOX5PeO5vT\n" +
                "DbDwqlB4Jym8YQIWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYF0D670BVvjy7x3mwqXL1vxZK3lv\n" +
                "3wuviNkBli3aj7RK9wdNALAAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7B0DcACLMACLMACLMACLMAyaAHLvgMWYAEWYAEWYAEWYAEWYAEWYAEWYHVtara7Fc7Uj3Ak64mV\n" +
                "dk8J37Rp4W6u3B8K2TS9xe/dCgALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsF4A1rRm3pup2Z61\n" +
                "snTZ60chLrP7Xlh17wk+u/KFBza7mx9Z5KoJC1iABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViAVdTy\n" +
                "CjGUbbj/XSU7aKenxcoPZddzZem+nMIOttIVp9vvGZdfmrCABViABViAJYAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAlgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVY7wzvwhP+kU60su+F5zZLh7MFmT7aZ6WY/aLsKmWtf7abZ+2isIdMj3LAAizAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAiz7DliABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABVi9y/retFhZ5JV2X8jQrM9Waqlwrkxf0gpPh3vO6MpP/2UBsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsACrAlh/Vync/pWdzg6bM3GugKCwZxU2x/c6w8puTr9S4cp/+e8F2X2v\n" +
                "GuWABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViT6jqrs7MJlB1LhcTJNtwv71Ehl6f79crpONv3\n" +
                "j9yyVio5i7bsDwEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYP0QWIX7d/ZF2en7Xi8oLJssGQsL\n" +
                "rJC22TE/XckrWF85sNmqmyZ4tqn+2+0ALMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMD6OUfOavfs\n" +
                "hE8XX+EJP9vi6W//bzmFNi1ERnYorqz8yph4ryuu3MMP3hOwAAuwAAuwAAuwAAuwAAuwAAuwAAuw\n" +
                "AAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuw\n" +
                "AAuwAGtyfBZ2ouygLdThymDI9tbCO8nKe753YD+CoWyzeu9SUfhHhMKpDViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViA1Qus6Z41zZHsgrzXhQuH90qFTGPoI1eaj7B+ZYs/4p7CmxtgARZgARZgqU/A\n" +
                "AizAAizAAizAAizAAizAAizAAizAMsAAC7AAC7AAC7AAC7AAC7AAS30CFmABFmABFmABFmABFmAB\n" +
                "FmABFmABlgEGWIAFWIAFWID1Q2CdJXseUltVW2crQ7FQsYVHZiXvMbTwgprVYeFufpl3hV2x/3AB\n" +
                "FmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmABFmABFmABFmABFmABFmABFmABFmAVnZzseShU1/RnTg/vs/+zsL8ULvJ7Yylb8yu7+d7d\n" +
                "qbB7F3aG/vUELMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAqysr4XFFCdgIVemJ6LE2/0spUWzmG\n" +
                "K59ZKKSP4DJbdSu3F8ACLMACLMACLMACLMACLMACLMACLMDymYAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWOQBWIAFWIAFWIAFWIAFWIAFWIAFWD8E1nszoLAXFH7mR8b8Cuuz\n" +
                "1i8c84WnY/rb3xveWR2uyLjQu/31CViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViAZfoCFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB1mTTyW7VRxYk\n" +
                "e26z+55d5OysyvbWlT1a+fZCDBUKqVAz0wW2siCABViABViABViABViABViABViABViABViABViA\n" +
                "xROABViABViABViABViABViABViABViABViABViABViABViABViABViABVgWBLAAC7AAC7AAC7B+\n" +
                "CKzsYS5see/9erblFZZNtrynvyjbr6e/qHCVpi8VH+mKKz3kpVoCLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMDKnPAVOpzV7vQZK0TGR+RR2FtX/nm2XxfOlezheo/LKzfMwqZaWHWABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABViABVi9m3p2SKq2qqGVZA9JdlqscLkQQyu1NE2clZWfFlJhByuskOzgy648\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAHWdekXzr+Vf14IgmltF3aiLJuymllZ+ZUvKrzjrVTd\n" +
                "dKdd6cnTlQxYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgOXYAxZgARZgARZgAVZFuz9rZNlDkj23K/3l7J9Pt/vCl1+p\n" +
                "upV2sUKcwov0CgSzZ3NlRBZeqAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsH6Y7KDNYmiaodkR\n" +
                "krVpFhnT27HSHAuH4nvbMU2x/olee9MorOSDYwhYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgJVp\n" +
                "T4XNMfvthSensBMVtpKVS8VK0a6c4sICe88TK2OikA7Zop3+cwNgARZgARZgARZgARZgARZgARZg\n" +
                "ARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZg\n" +
                "ARZgARZgAdZYF8423OygLeyYhd1tev5Ne3dFM4XXucILgJtwPzKyBTZN24MFASzAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAyjTcs8XKDoZs6U/36/f26MvyqGqOtVkZ3oVHZuX+QHJvnHfAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAqwMXM6enC6+s+m7oplpIWUrZOVKU3hgs+2+8Ir4kVOcHT2FzWp6\n" +
                "vv/blQcswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswPp5TXzkkGRH3dlcee/bV7a4cKJnz3t2QbIV\n" +
                "UjiWvrzvK40ly6aVSgYswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs+w5YgNXbXwqTHTYrrbkQbSvqKoTL\n" +
                "9BavzOnsACusecUw54naCkltMWABFmABFmABFmABFmABFmABFmABlpkKWIoBsAALsAALsADLTAUs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswFIMgAVYgAVYgAVYZipgKQbAAqx/uVi/2P7CuVI4\n" +
                "lt5ruGfFcLby2ea4clVY+eeFRasJPP+eKwPl+aYKWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWICV\n" +
                "0UwhxbLHKTt9VwbDSiW/N9XOvr3w6pXt7NMNsJC2K+135Rr/zZYOWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIDVjozCVpL9ouwhWfmh6aV7bztWDtd7pfgRQ6/s0ZngC68fL307YAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAHWz9tTYb/OtpIs77KdfXosFbo8W97Tc3r6xE23tekj89416ctjArAAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AcGcACLMACLCcHsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsADLkQEswPohsKYpVtggCtdzWjPvdbez95zOytKtMJS2Uz0ku8Ur4qw6\n" +
                "m4AFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWO1VflbQ2TornAGFXC78ounrR+GT2bFU+PLZW1Yh\n" +
                "mwq9++XP/EinBSzAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizy8JmABViABViA\n" +
                "BViABVg+E7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7DIA7AA64WayM6qbB8sRFsh1guP/ZmMC/OR\n" +
                "STndwQqXLgvW6Rtm4UQonJuABViABViABViABViABViABViABViABViABViABViABViABViABViA\n" +
                "BViABViABViABViABViABViABViABViABViABViABViABViABViABViABVjXwMr6rLB2V8B6xqZC\n" +
                "S325t07Toaq3bnWw917+I5eKwpdfOcUHLw9YgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVY\n" +
                "gAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgFUErOwP\n" +
                "FeLyIwMsq8OVplOIjOkBtnJNymbl/mA3+1f+pQAWYAEWYAGWkQxYdhOwAAuwAAuwAAuwAMtuAhZg\n" +
                "ARZgARZgARZgARZgARZgARZgARZgGcmAZTcBC7AAC7AAC7CMZMCym4AFWIAFWIAFWIAFWIAFWIDV\n" +
                "PsAK5THdSrKdaGU9zar+AVa4ntP3sbNFfu+atPKZZ6803cEAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7B+WHxZ96wQ5yPiLOxu2RFytiAr43O6wKZZr8Cqymalz2evH/3jDLAAC7AAC7AAS4EBFmAB\n" +
                "FmABFmCZf4AFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWAoMsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsDLd7aykztrTWXcrnGrvvfyXQVA4aKcH2EeaVbYzrEBwZSKs9KVUeQMWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYAEWYAEWYAEWYP28QWTb6Hsz4Kw5Zrn8Xsf8iPWztVQ4LdT8S+do5eo1fako\n" +
                "3A7AAizAAizAAizAAizAAizAAizDBrAAS80DFmABFmABFmABFmABFmABFmABFmABFmABFmABFmAB\n" +
                "FmABFmAZNoAFWGoesAALsAALsAALsAALsAALsADruiayvXUlhXW2Mj7fI3iWy9PqKvz1j7z8R7Z4\n" +
                "ZZgW/tDZvgMWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEWYAEW\n" +
                "YAEWYAEWYAEWYAEWYJm+gAVYgAVYgAVYgAVYgAVYgAVYgFUBrOlhc3ZEs7O/sD0VVl224Wb3qLAU\n" +
                "Vw7Cyn1smiNfhmB2oBTyrn/lAQuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuw\n" +
                "AAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuwAAuw3pmp04Xy3hl7b6oV\n" +
                "TgsMHW0X2c5QSJyVYlhZ+RVDj6oLsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsACrvUGclVT2n2eP\n" +
                "UyFH3puUZ5pZKdqVH1ohzsrFb6XPr/y5oVCxK5c0wAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAKs\n" +
                "HwJrpY1mZ/9KczzbzUIlFA6bv6sUtub3Btj0qCs8xSurVMiRwg5W2FgAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AA\n" +
                "C7AAC7AAC7AAC7B+CKyVOT1dKGffXniYp0dIdqK/J7mzwbByYN/7ovcuAFkIurUe1DxgARZgARZg\n" +
                "ARZgARZgARZgARZgARaOABZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARZgARaO\n" +
                "ABZgARZgARZgARZgARZgARZgAVZFTaxM9IOtOquJ321HdqJP96zsWFo5XFmjrCgh24JWBF94jS/8\n" +
                "P9+7uf3jrwYswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswFpM1nyFksvatLCVvMflbNmszP4VBBde\n" +
                "fqZvg9k7SWHRrtRSYfcGLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMAC\n" +
                "LMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMACLMCqaGRngyF7brPmKxTnl5/M\n" +
                "trzCgzDN5ffUVejywkvae3B5T7Gp/glYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYgAVYngQs\n" +
                "wAIswAIswAIswIIhwAIswAIswAIswAIswAIswAIswAIswAIswAIsTwIWYAEWYAEWYGU2tfA4FXai\n" +
                "7CJnFVs4Qqbn30rZFP769DkqHJ9Vk7L25vaR+8OoTQELsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAALsAAL\n" +
                "sNo5UjiSV6Zv9jML33Ol6WTPkaJNXaim6VB4tKevXmfi/MgpBizAAizAAizAAizAAizAAizAAizA\n" +
                "AizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAizAAiyzCrAAC7AAC7AAC7AAC7AAC7AU\n" +
                "LWABFmABFmDNVGTh8H7vM79cimdjvpCMK+2+kPWF98aVa3z25bNs+ohiAQuwAEspAhZgARZgARZg\n" +
                "ARZgKUXAAizAAizAAixTDbAAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AAC7AASykCFmABFmABFmCZ\n" +
                "aoAFWIAFWIAFWIAFWKZaGC5Zo3zZPYUb95F9X8GlUZdakJVSXCmbbH1mfyhVtIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWEUzIDvAzsZ84azKFnT2hKulqvNeeMv6yEjO3p0K28XKRdrK33cwwAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIswAIs\n" +
                "wAIswAIswAIswAIswAIswAIswAIswAKsov5SqIRs7f4XzS/69dn/mR3JK3uUbQJfLsX3jkzh0hVe\n" +
                "+AsVu3Jgq8wHWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAF\n" +
                "WIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAFWIAlIiIiIoAlIiIiAlgiIiIigCUiIiIi\n" +
                "gCUiIiICWCIiIiKAJSIiIgJYIiIiIgJYIiIiIoAlIiIiAlgiIiIiAlgiIiIigCUiIiICWCIiIiIC\n" +
                "WCIiIiKAJSIiIgJYIiIiIgJYIiIiIoAlIiIiMpP/AWByg3KYtjEYAAAAAElFTkSuQmCC";
        String deEncodeByBase64 = QRCodeUtil.deEncodeByBase64(code);
        byte[] decode3 = Base64.getDecoder().decode(deEncodeByBase64);
        String decode3Str = URLDecoder.decode(new String(decode3), "UTF-8");
        System.out.println(decode3Str);
    }


}

