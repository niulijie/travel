package com.niulijie.springboot.service;

import com.google.zxing.WriterException;
import com.niulijie.springboot.entity.DaPrPost;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 警力报备处理接口层
 * @author df
 * @date 2021/12/25
 */
public interface PoliceReportService {

    /**
     * 获取岗位列表用于生成二维码并转换成base64存储
     * @param daPrPosts
     */
    List<DaPrPost> generatePostQPCodeWith64(List<DaPrPost> daPrPosts);

    void generatePostQPCode() throws IOException, WriterException;

    void testDecode() throws Exception;
}
