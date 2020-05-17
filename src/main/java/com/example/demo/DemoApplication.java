package com.example.demo;

import com.example.demo.Dao.UserDOMapper;
import com.example.demo.dataObject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.example.demo"})
@RestController
@MapperScan("com.example.demo.Dao")
public class DemoApplication {

    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String home() {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO==null) {
            return "用户对象不存在";
        } else return userDO.getName();
    }
    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

}
