package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.user.User;
import com.qingcheng.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping("/sendSms")
    public Result sendSms(String phone){
        userService.sendSms(phone);
        return new Result();
    }

    @PostMapping("/register")
    public Result save(@RequestBody User user, String smsCode){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPwd = encoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        userService.add(user,smsCode);
        return new Result();
    }

}
