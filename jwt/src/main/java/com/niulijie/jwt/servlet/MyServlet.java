package com.niulijie.jwt.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author niuli
 */
@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 输出sessionId
        System.out.println("sessionId="+req.getSession().getId());
        //转发到show.jsp
        req.getRequestDispatcher("/show.jsp").forward(req, resp);
    }
}
