package com.example.demo.service;

import com.example.demo.service.model.UserModel;
import org.springframework.stereotype.Service;


public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel getUserById(Integer id);
}
