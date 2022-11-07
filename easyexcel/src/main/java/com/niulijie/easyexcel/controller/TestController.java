package com.niulijie.easyexcel.controller;

import com.niulijie.easyexcel.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/download")
    public String windowsClientDownload(HttpServletResponse response, Long procedureId) {
        testService.doDownload(response);
        return "666";
    }

    /**
     * 下载
     * @return
     */
    @GetMapping("/download2")
    public String download(HttpServletResponse response){
        testService.downloadKolList(response);
        return "8888";
    }
    /*@GetMapping("/exportUserTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        //导出模板名称
        String  fileName="测试.xlsx";
        response.setContentType("multipart/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //编码 防止中文乱码
        try {
            fileName = URLEncoder.encode(fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition","attachment;filename="+fileName);
        //excel表头数据
        List<UserDownloadTemplateVO> userDownloadTemplateVOList = new ArrayList<>();
        try {
            //xxxx表示excel下拉框动态数据（一般都是从数据库查出）
            //xxx表示模板里sheet的名称
            EasyExcel.write(response.getOutputStream(),UserDownloadTemplateVO.class)
                    .registerWriteHandler(new CustomSheetWriteHandler(xxxx))
                    .sheet("xxx")
                    .doWrite(userDownloadTemplateVOList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
