package com.niulijie.mdm.dto.response;

import lombok.Data;

@Data
public class UploadFileResp {

    /**
     * 视频标题-存储文件名
     */
    private String title;

    /**
     * 暂定文件名
     */
    private String fileName;

    /**
     * 视频的云端地址
     */
    private String videoUrl;

    /**
     * 视频md5
     */
    private String videoMd5;

    /**
     * 图片的云端地址
     */
    private String pictureUrl;

}
