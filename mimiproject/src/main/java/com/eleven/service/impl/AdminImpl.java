package com.eleven.service.impl;

import com.eleven.dao.AdminMapper;
import com.eleven.domain.Admin;
import com.eleven.domain.AdminExample;
import com.eleven.service.AdminService;
import com.eleven.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Override
    public Admin login(String name, String pwd) {
        //获取密码的加密
        String password  = MD5Util.getMD5(pwd);
        //
        AdminExample adminExample  = new AdminExample();
        //添加条件
        adminExample.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        if(list.size()>0){
            Admin admin = list.get(0);
            String s = admin.getaPass();
            if (password.equals(admin.getaPass())){
                return admin;
            }
        }
        return null;
    }
}
