package com.example.demo.controller;

import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessErr;
import com.example.demo.resopnse.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

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
