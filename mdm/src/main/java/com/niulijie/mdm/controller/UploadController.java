package com.niulijie.mdm.controller;

import com.niulijie.mdm.dto.param.ContVideoParam;
import com.niulijie.mdm.dto.request.FileBatchUploadRequest;
import com.niulijie.mdm.result.BaseResult;
import com.niulijie.mdm.result.ResultUtil;
import com.niulijie.mdm.service.ContVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class UploadController {

    @Autowired
    private ContVideoService contVideoService;

    /**
     * 上传视频文件接口
     * @param request 请求Request
     * @return
     */
    @PostMapping(value = "/upload")
    public BaseResult upload(@Valid FileBatchUploadRequest request) throws ExecutionException, InterruptedException {
        return ResultUtil.ok(contVideoService.upload(request));
    }

    /**
     * 批量获取视频信息通过Md5值
     */
    @PostMapping("/batch/check/md5")
    public BaseResult batchCheckMd5(@Validated(ContVideoParam.PcCheckMd5.class)
                                    @RequestBody ContVideoParam checkParam) {
        return ResultUtil.ok(contVideoService.batchCheckMd5(checkParam));
    }

}
