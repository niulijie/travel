package com.niulijie.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.check.platform.utils
 * @email zhoupengbing@telecomyt.com.cn
 * @description 图片工具类
 * @createTime 2021年05月14日 18:54:00
 * @Version v1.0
 */
public class ImageUtils {

    /**
     * @Function: 将一张网络图片转化成Base64字符串
     * @param imgSrc - 网络图片资源位置
     */
    public static String getImageBase64StrFromUrl(String imgSrc) throws Exception {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try{
            url = new URL(imgSrc);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestMethod("GET");
            httpUrl.setConnectTimeout(30 * 1000);
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[2048];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while( (len=is.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
        }catch (Exception e) {
            throw new Exception(e);
        }finally{
            if(is != null) {
                try {is.close(); } catch (IOException e) {e.printStackTrace(); }
            }
            if(outStream != null) {
                try {outStream.close();} catch (IOException e) {e.printStackTrace();}
            }
            if(httpUrl != null){ httpUrl.disconnect();}
        }
        // 对字节数组Base64编码
        return java.util.Base64.getEncoder().encodeToString(outStream.toByteArray());

    }


    /**
     * 根据url将标准图压缩
     *
     * @param url 图片地址
     */
    public static void zoom(String url) {
        if (!url.contains("png")) {
            if (!StringUtils.isBlank(url)) {
                //地址转换为本地全路径地址
                //图片压缩并转存到原路径
                try {
                    Thumbnails.of(url)
                            .scale(1f)
                            .outputQuality(0.25f)
                            .toFile(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(url);
            }
         }
      }
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr 图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        // 图像数据为空
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 生成jpg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }
}
