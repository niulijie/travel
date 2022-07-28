package com.niulijie.mdm.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 上传文件请求实体类
 * @author niuli
 */
@Data
public class FileBatchUploadRequest {

    /**
     *  文件
     */
    @NotEmpty(message = "上传文件不能为空")
    private List<MultipartFile> files;

}
