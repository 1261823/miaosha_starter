package com.example.demo.service.impl;

import com.example.demo.Dao.UserDOMapper;
import com.example.demo.Dao.UserPasswordDOMapper;
import com.example.demo.dataObject.UserDO;
import com.example.demo.dataObject.UserPasswordDO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessErr;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import com.example.demo.validator.ValidationResult;
import com.example.demo.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDOMapper userDOMapper;

    @Autowired
    UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    ValidatorImpl validator;


    @Override
    public UserModel getUserById(Integer id) {
        // 调用userDOmapper获取到对应的用户的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO==null) return null;
        // 通过用户id获取对应密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR);
//        if(StringUtils.isEmpty(userModel.getName()) || userModel.getGender()==null
//        || userModel.getAge()==null || StringUtils.isEmpty(userModel.getTelephone())) {
//            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR);
//        }

        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }


        // 实现model到dataobject的方法
        UserDO userDO = convertFromModel(userModel);
        userDOMapper.insertSelective(userDO);
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessErr.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessErr.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel) {
        if (userModel==null) return null;
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);

        return userDO;
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
