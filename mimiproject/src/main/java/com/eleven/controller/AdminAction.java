package com.eleven.controller;

import com.eleven.domain.Admin;
import com.eleven.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {
    @Autowired
    AdminService service;

    @RequestMapping("/login.action")
    public String login(HttpServletRequest req,String name, String pwd){
        //返回admin
        Admin admin = service.login(name,pwd);
        if(admin!=null){
            //登陆成功
            req.setAttribute("admin",admin);
            req.setAttribute("name",name);
            String n = admin.getaName();
            String p = admin.getaPass();
            return "main";
        }else {
            req.setAttribute("errmsg","用户名或者密码不正确!");
            return "login";

        }
    }
}
