package com.niulijie.mdm.util;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.niulijie.mdm.config.MinioConfig;
import com.niulijie.mdm.result.BusinessException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * minio工具类
 *
 * @date 2022/06/13
 */
@Component
@Slf4j
public class MinioUtil {

    private final MinioConfig prop;

    private final MinioClient minioClient;

    /**
     * 转化图片后缀
     */
    private final String picType = ".jpg";

    public MinioUtil(MinioConfig prop, MinioClient minioClient) {
        this.prop = prop;
        this.minioClient = minioClient;
    }

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("查看存储bucket是否存在异常:{}", e.getMessage());
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("创建存储bucket异常:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error("删除存储bucket异常:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 图文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String uploadPic(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new BusinessException(400, "没有获取到文件");
        }
        String fileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = date() + RandomUtil.randomString(4) + fileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error("文件上传异常:{}", e.getMessage());
        }
        return objectName;
    }

    /**
     * 视频文件上传
     *
     * @param file 文件
     * @param title
     * @return Boolean
     */
    public String uploadVideo(MultipartFile file, String title) {
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(title)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error("视频文件上传异常:{}", e.getMessage());
            return "";
        }
        return prop.getEndpoint() + prop.getBucketName() + "/" + title;
    }

    /**
     * 格式化时间
     * @return
     */
    public String date(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return format.format(new Date());
    }

    /**
     * 预览图片
     *
     * @param fileName
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder()
                .bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.error("预览图片异常:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void downloadFile(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            log.error("文件下载异常:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            log.error("查看文件对象异常:{}", e.getMessage());
            return Collections.emptyList();
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     */
    public boolean removeFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件流
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 断点下载
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param offset 起始字节的位置
     * @param length 要读取的长度
     * @return 流
     */
    public InputStream getObject(String bucketName, String objectName, long offset, long length) throws IOException, InvalidKeyException,
            InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            ErrorResponseException {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }

    /**
     * 将远程视频得某帧作为缩略图
     * @param videoUrl 视频文件远程地址
     * @param title 视频文件名称
     * @return
     */
    public String updatePicByUrl(String videoUrl, String title) {
        //图片端地址
        String imageUrl = "";
        // 取得文件名
        String fileName = title.substring(0, title.lastIndexOf(".")) + picType;
        try {
            ByteArrayOutputStream tempPath = ImageUtils.getTempPath(videoUrl);
            //创建ByteArrayResource用ByteArray输出流的字节数组
            InputStreamSource inputStreamSource = new ByteArrayResource(tempPath.toByteArray());
            //图片上传
            String imageUri = putObject(prop.getBucketName(), fileName, inputStreamSource.getInputStream());
            imageUrl = prop.getEndpoint() + imageUri;
        } catch (Exception e) {
            log.error("视频缩略图文件上传异常:{}", e.getMessage());
            return imageUrl;
        }
        return imageUrl;
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName 存储桶
     * @param objectName 文件对象
     * @param inputStream 文件流
     */
    public String putObject(String bucketName, String objectName, InputStream inputStream)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, ErrorResponseException {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                inputStream, inputStream.available(), -1).contentType(MediaType.IMAGE_JPEG_VALUE)
                .build());
        return bucketName + "/" +  objectName;
    }

    /**
     * 拷贝文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param srcBucketName 目标bucket名称
     * @param srcObjectName 目标文件名称
     */
    public ObjectWriteResponse copyObject(String bucketName, String objectName,String srcBucketName, String srcObjectName)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        return minioClient.copyObject(CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                        .bucket(srcBucketName)
                        .object(srcObjectName)
                        .build());
    }


}
