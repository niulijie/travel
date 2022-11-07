package com.niulijie.easyexcel.service;

import javax.servlet.http.HttpServletResponse;

public interface TestService {

    void doDownload(HttpServletResponse response);

    void downloadKolList(HttpServletResponse response);
}
