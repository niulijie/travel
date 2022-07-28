package com.niulijie.mdm.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

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
     * 压缩率
     */
    private static final transient  float IMAGE_RATIO=0.1f;
    /**
     * 压缩最大宽度
     */
    private static final transient  int IMAGE_WIDTH=800;
    /**
     * 水印透明度
     */
    private static float alpha = 0.3f;
    /**
     * 水印文字字体
     */
    private static Font font = new Font("PingFang SC Regular", Font.PLAIN, 36);
    /**
     * 水印文字颜色
     */
    private static Color color = new Color(111, 111, 111);
    /**
     * 水印文字内容
     */
    private static final String text="这是一个水印文本";
    /**
     * 水印之间的间隔
     */
    private static final int XMOVE = 80;
    /**
     * 水印之间的间隔
     */
    private static final int YMOVE = 80;
    /**
     * 转化图片后缀
     */
    private static final String picType = "jpg";

    /**
     *
     * @Title: getTempPath
     * @Description: 生成视频的首帧图片方法
     * @author: Zing
     * @param: @param tempPath 生成首帧图片的文件地址
     * @param: @param filePath 传进来的线上文件
     * @param: @return
     * @param: @throws Exception
     * @return: boolean
     * @throws
     */
    public static ByteArrayOutputStream getTempPath(String filePath) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //判断文件是否为视频
        if(isVideo(filePath)) {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(filePath);
            ff.start();
            //获取视频总帧数
            int ftp = ff.getLengthInFrames();
            int flag=0;
            Frame frame = null;
            while (flag <= ftp) {
                //获取帧
                frame = ff.grabImage();
                //过滤前5帧，避免出现全黑图片
                if ((flag>5)&&(frame != null)) {
                    break;
                }
                flag++;
            }
            if(ImageIO.write(FrameToBufferedImage(frame), picType, byteArrayOutputStream)) {
                ff.close();
                ff.stop();
            }else {
                ff.close();
                ff.stop();
            }

        }
        return byteArrayOutputStream;
    }

    /***
     *
     * @Title: FrameToBufferedImage
     * @Description: 创建格式化BufferedImage对象
     * @author: Zing
     * @param: @param frame
     * @param: @return
     * @return: RenderedImage
     * @throws
     */
    private static RenderedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    /**
     * 压缩图像
     * @param image
     * @return
     * @throws IOException
     */
    /*public static BufferedImage compress(BufferedImage image) throws IOException {
        Thumbnails.Builder<BufferedImage> imageBuilder= Thumbnails.of(image).outputQuality(IMAGE_RATIO);
        if(image.getWidth()>IMAGE_WIDTH){
            return imageBuilder.width(IMAGE_WIDTH).asBufferedImage();
        }
        else {
            return imageBuilder.scale(1).asBufferedImage();
        }
    }*/

    /**
     * 图像添加水印
     * @param
     * @return
     */
    /*public static BufferedImage setWatermark(BufferedImage image)throws IOException {
        return Thumbnails.of(image)
                .outputQuality(IMAGE_RATIO)
                .scale(1)
                .watermark(Positions.BOTTOM_RIGHT
                        ,createWatermark(text
                                ,image.getWidth()
                                ,image.getHeight()
                        )
                        ,alpha)
                .asBufferedImage();
    }*/

    /**
     * 根据文件扩展名判断文件是否图片格式
     * @return
     */
    public static boolean isImage(String fileName) {
        String[] imageExtension = new String[]{"jpeg", "jpg", "gif", "bmp", "png"};

        for (String e : imageExtension) {
            if (getFileExtention(fileName).toLowerCase().equals(e)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取文件后缀名称
     * @param fileName
     * @return
     */
    public static String getFileExtention(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }

    /**
     * 根据图片对象获取对应InputStream
     *
     * @param image
     * @param readImageFormat
     * @return
     * @throws IOException
     */

    public static InputStream getInputStream(BufferedImage image, String readImageFormat) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, readImageFormat, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return is;
    }

    /**
     * 创建水印图片
     * @param text 水印文字
     * @param width 图片宽
     * @param height 图片高
     * @return
     */
    public static BufferedImage createWatermark(String text,int width,int height)  {
        BufferedImage image = new BufferedImage(width
                , height, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics2D g = image.createGraphics();
        // ----------  增加下面的代码使得背景透明  -----------------
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        g = image.createGraphics();
        // ----------  背景透明代码结束  -----------------
        // 6、处理文字
        AttributedString ats = new AttributedString(text);
        ats.addAttribute(TextAttribute.FONT, font, 0, text.length());
        AttributedCharacterIterator iter = ats.getIterator();
        // 7、设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 8、设置水印旋转
        g.rotate(Math.toRadians(-30));
        // 9、设置水印文字颜色
        g.setColor(color);
        // 10、设置水印文字Font
        g.setFont(font);
        // 11、设置水印文字透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        /**
         * 水印铺满图片
         * 计算水印位置
         */
        int x = -width / 2;
        int y = -height / 2;
        int[] arr = getWidthAndHeight(text, font);
        int markWidth = arr[0];// 字体长度
        int markHeight = arr[1];// 字体高度
        // 循环添加水印
        while (x < width * 1.5) {
            y = -height / 2;
            while (y < height * 1.5) {
                g.drawString (text, x, y);

                y += markHeight + YMOVE;
            }
            x += markWidth + XMOVE;
        }
        // 13、释放资源
        g.dispose();
        return image;
    }

    /**
     * 计算字体宽度及高度
     * @param text
     * @param font
     * @return
     */
    private static int[] getWidthAndHeight(String text, Font font) {
        Rectangle2D r = font.getStringBounds(text, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());//
        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        // 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int height = unitHeight + 3;
        return new int[]{width, height};
    }

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

    public static InputStream getInputStreamFromUrl(String imgSrc) throws Exception {
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
        return is;

    }

    /**
     * 根据url将标准图压缩
     *
     * @param url 图片地址
     */
    /*public static void zoom(String url) {
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
    }*/

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

    /**
     *
     * @Title: isVideo
     * @Description:判断是不是视频
     * @author: Zing
     * @param: @param path 文件路径
     * @param: @return
     * @return: boolean       true是视频 false非视频
     * @throws
     */
    public static boolean isVideo(String path) {
        List<String> typeList = new ArrayList();
        typeList.add("mp4");
        typeList.add("flv");
        typeList.add("avi");
        typeList.add("rmvb");
        typeList.add("rm");
        typeList.add("wmv");
        //获取文件名的后缀
        String suffix = path.substring(path.lastIndexOf(".") + 1);
        for(String type : typeList) {
            if(type.toUpperCase().equals(suffix.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
