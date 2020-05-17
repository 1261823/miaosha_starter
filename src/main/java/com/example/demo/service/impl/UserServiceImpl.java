package com.example.demo.service.impl;

import com.example.demo.Dao.UserDOMapper;
import com.example.demo.Dao.UserPasswordDOMapper;
import com.example.demo.dataObject.UserDO;
import com.example.demo.dataObject.UserPasswordDO;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDOMapper userDOMapper;

    @Autowired
    UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        // 调用userDOmapper获取到对应的用户的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO==null) return null;
        // 通过用户id获取对应密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
