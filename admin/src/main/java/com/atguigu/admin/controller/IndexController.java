package com.atguigu.admin.controller;

import com.atguigu.admin.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class IndexController {

    /**
     * 登录页
     * @return
     */
    @GetMapping(value = {"/","/login"})
    public String loginPage(){
        return "login.html";
    }

    /**
     * 主页
     * @param userName
     * @param password
     * @return
     */
    /*@PostMapping("/login")
    public String main(String userName, String password){
        //登录成功后重定向到main.html，重定向防止表单重复提交
        return "redirect:main.html";
    }*/

    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model){

        if(StringUtils.hasLength(user.getUserName()) && "123456".equals(user.getPassword())){
            //把登陆成功的用户保存起来
            session.setAttribute("loginUser",user);
            //登录成功重定向到main.html;  重定向防止表单重复提交
            return "redirect:/main.html";
        }else {
            model.addAttribute("msg","账号密码错误");
            //回到登录页面
            return "login";
        }

    }

    /**
     * 重定向处理，main.html刷新后还是在main页面
     * @return
     */
    @GetMapping("/main.html")
    public String mainPage(HttpSession session, Model model){
        log.info("当前方法是：{}", "mainPage");
        //是否登录，拦截器，过滤器
        /*Object loginUser = session.getAttribute("loginUser");
        if (Objects.isNull(loginUser)) {
            model.addAttribute("msg","请重新登录");
            //回到登录页面
            return "login";
        }else {
            return "main";
        }*/
        return "main";
    }

}
