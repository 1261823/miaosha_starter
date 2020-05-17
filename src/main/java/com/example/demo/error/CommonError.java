package com.example.demo.error;

public interface CommonError {
    public int getErrorCode();
    public String getErromMsg();
    public CommonError setErrMsg(String errMsg);
}
