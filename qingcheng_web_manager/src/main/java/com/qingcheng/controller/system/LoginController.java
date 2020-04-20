package com.qingcheng.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.system.LoginLog;
import com.qingcheng.service.system.LoginLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    private LoginLogService loginLogService;

    @GetMapping("/findAll")
    public List<LoginLog> findAll(){
        return loginLogService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<LoginLog> findPage(int page, int size){
        return loginLogService.findPage(page, size);
    }

    @GetMapping("/name")
    public Map showName(){
        System.out.println("showName方法标记");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("username",name);
        return map;
    }

    @GetMapping("/getLoginLog")
    public PageResult<LoginLog> getLoginLog(int page,int size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("loginName",name);
        return loginLogService.findPage(map,page,size);
    }
}

