package com.example.demo.resopnse;

public class CommonReturnType {
    // 表明对应请求的返回处理结果“sucess”或“fail”
    private String status;

    // 若status=sucess，data中返回前端需要数据
    // 若 status=fail，data中使用通用错误码格式
    private Object data;

    // 定义通用创建方法
    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }
    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
