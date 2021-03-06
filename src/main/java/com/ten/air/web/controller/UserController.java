package com.ten.air.web.controller;

import com.ten.air.web.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录验证的controller控制器
 */
public class UserController extends HttpServlet {

    private volatile UserService service = new UserService();

    /**
     * 登录成功 -> 发送record.do请求, 跳转到data.jsp
     */
    private static final String SUCCESS = "/record.do";
    /**
     * 登录失败 -> 跳转到登录界面index.jsp
     */
    private static final String FAIL = "/index.jsp";

    /**
     * 登录校验
     *
     * @param req [name, password]
     * @mapping /login.do
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("Username");
        String password = req.getParameter("Password");
        System.out.println("Recieve New Login Request:" + name + "," + password);

        // 登录校验
        boolean success = service.login(name, password);

        if (success) {
            System.out.println("User Login Success:" + name + "," + password);
            req.getRequestDispatcher(SUCCESS).forward(req, resp);
        } else {
            System.out.println("User Login Failure:" + name + "," + password);
            // alert弹出提示信息
            req.setAttribute("errormsg", "登录失败");
            req.getRequestDispatcher(FAIL).forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
