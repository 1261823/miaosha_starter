package com.example.demo.controller;

import com.example.demo.controller.viewobject.UserVO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessErr;
import com.example.demo.resopnse.CommonReturnType;
import com.example.demo.service.UserService;
import com.example.demo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (userModel==null) {
            throw new BusinessException(EmBusinessErr.USER_NOT_EXIST);
        }
        // 讲核心领域用户model转换为viewobject
        UserVO userVO = convertFromModel(userModel);

        // 返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel==null) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    //定义exceptionhandler解决未被controller层级吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode", businessException.getErrorCode());
            responseData.put("errMsg", businessException.getErromMsg());
        } else {
            responseData.put("errCode", EmBusinessErr.UNKNOW_ERROR.getErrorCode());
            responseData.put("errMsg", EmBusinessErr.UNKNOW_ERROR.getErromMsg());
        }

        return CommonReturnType.create(responseData,"fail");
    }
}
