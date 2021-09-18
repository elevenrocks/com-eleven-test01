package com.eleven.service;

import com.eleven.domain.Admin;



public interface AdminService {
    Admin login(String name, String pwd);
}
