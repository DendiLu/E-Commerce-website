package com.qingcheng.controller;

import com.qingcheng.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice//表示控制器的通知类
public class BaseExceptionHandler {
    @ExceptionHandler(Exception.class)//指定监测的异常类型
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        System.out.println("调用了异常处理");
        return new Result(1,e.getMessage());
    }
}
