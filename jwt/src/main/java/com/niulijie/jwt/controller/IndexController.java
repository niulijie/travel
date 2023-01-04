package com.niulijie.jwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 1.jsp跳转默认位置src/main/webapp，所以创建webapp目录，注意层级
 * @author niuli
 */
@Controller
//@RestController 需要返回ModelAndView
public class IndexController {

    /**
     * 登录页
     * @return
     */
    @RequestMapping(value = {"/test"})
    public String loginPage(){
//        ModelAndView index = new ModelAndView("index");
//        return index;
        return "index";
    }

    @RequestMapping(value = {"/my/servlet"})
    public void myServlet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ModelAndView index = new ModelAndView("index");
//        return index;
        // 输出sessionId
        System.out.println("sessionId="+req.getSession().getId());
        //转发到show.jsp
        req.getRequestDispatcher("/show.jsp").forward(req, resp);
    }
}
