package com.niulijie.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.niulijie.common.properties.FileProperties;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Component
public class QRCodeUtil {

    private static QRCodeUtil qrCodeUtil;

    @Autowired
    private FileProperties fileProperties;

    @PostConstruct
    public void init() {
        qrCodeUtil = this;
        qrCodeUtil.fileProperties = this.fileProperties;
    }

    private static final String CHARSET = "utf-8";

    private static final String FORMAT_NAME = "PNG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 800;
    // LOGO宽度
    private static final int WIDTH = 120;
    // LOGO高度
    private static final int HEIGHT = 120;

    /**
     *
     * @param content 二维码的内容
     * @param imgPath 嵌入二维码的图片路径
     * @param name 二维码生成名字-用主键
     * @param needCompress
     * @throws Exception
     */
    public static String encode(String content, String imgPath, String name, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        String destPath = qrCodeUtil.fileProperties.getPath() + name + ".png";
        mkdirs(destPath);
        //写文件
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
        //将图片转换成Base64编码
        return imageToBase64ByLocal(destPath);
    }

    /**
     * 将文件内容生成二维码图片
     * @param content 内容
     * @param imgPath 二维码中间logo
     * @param needCompress 是否压缩
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        //指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        //边框间距px
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        //与注释掉部分功能一致
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
//        image = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 插入logo图片
     * @param source
     * @param imgPath
     * @param needCompress
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 将内容生成二维码图片，返回图片
     * @param content
     * @param imgPath
     * @param needCompress
     * @return
     * @throws Exception
     */
    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        return image;
    }

    /**
     * 创建文件
     * @param destPath
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成带无压缩logo的二维码图片文件
     * @param content
     * @param imgPath
     * @param destPath
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }

    /**
     * 生成无logo图片
     * @param content
     * @param destPath
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    /**
     * @param outputStream  可以来自response，也可以来自文件
     * @param contents      内容
     * @param margin        图片格式，可选[png,jpg,bmp]
     * @param format        图片格式，可选[png,jpg,bmp]
     * @param width         宽
     * @param height        高
     *                      eg:
     *                      ZxingKit.encodeOutPutSteam(response.getOutputStream(), qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200);
     */
    public static void encodeOutPutSteam(OutputStream outputStream, String contents, Integer margin, String format, int width, int height) {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = (new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

    }

    /**
     * 生成二维码输出流
     * @param content
     * @param imgPath
     * @param output
     * @param needCompress
     * @throws Exception
     */
    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成无logo图片文件流
     * @param content
     * @param output
     * @throws Exception
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    /**
     * 读取文件内容
     * @param file
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 根据文件路径读取文件内容
     * @param path
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

    /**
     * 本地图片转换成base64字符串
     *
     * @param path 图片位置
     * @reture 图片Base64
     * @author Byr
     * @dateTime 2019-03-07
     */
    public static String imageToBase64ByLocal(String path) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            //获取图片路径
            File file = new File(path);
            in = new FileInputStream(file.getPath());

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return "data:image/png;base64," + encoder.encode(data);
    }

    /**
     * 二维码解析,此方法是解析Base64格式二维码图片
     * baseStr:base64字符串,data:image/png;base64开头的
     */
    public static String deEncodeByBase64(String baseStr) {
        String content = null;
        BufferedImage image;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b=null;
        try {
            int i = baseStr.indexOf("data:image/png;base64,");
            baseStr = baseStr.substring(i+"data:image/png;base64,".length());//去掉base64图片的data:image/png;base64,部分才能转换为byte[]

            b = decoder.decodeBuffer(baseStr);//baseStr转byte[]
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);//byte[] 转BufferedImage
            image = ImageIO.read(byteArrayInputStream);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            //优化精度
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            //复杂模式，开启PURE_BARCODE模式
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
            content = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }
}

