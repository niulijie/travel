package com.niulijie.springboot.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.zxing.WriterException;
import com.niulijie.springboot.entity.DaPrPost;
import com.niulijie.springboot.mapper.DaPrPostMapper;
import com.niulijie.springboot.service.PoliceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 警力报备处理控制层
 * @date 2021/12/25
 */
@RestController
public class PoliceReportController {

    @Autowired
    private DaPrPostMapper daPrPostMapper;

    private final PoliceReportService policeReportService;

    public PoliceReportController(PoliceReportService policeReportService) {
        this.policeReportService = policeReportService;
    }

    @PostMapping("/test/encode")
    public void testEncode() throws Exception {
        List<DaPrPost> daPrPosts = daPrPostMapper.selectList(Wrappers.<DaPrPost>lambdaQuery().eq(DaPrPost::getPostId,1));
        policeReportService.generatePostQPCodeWith64(daPrPosts);
    }

    @PostMapping("/test/encode2")
    public void testEncode2() throws IOException, WriterException {
        policeReportService.generatePostQPCode();
    }

    @PostMapping("/test/decode")
    public void testDecode() throws Exception {
        policeReportService.testDecode();
    }

    /**
     * 测试解析base64二维码
     * @throws Exception
     */
    @PostMapping("/test/decode2")
    public void testDecode2() throws Exception {
        policeReportService.testDecode3();
    }
}
