package com.qingcheng.entity;

import java.io.Serializable;

/**
 * 返回前端的消息封装
 */
public class Result implements Serializable {
    private Integer code;//返回的业务码  0:成功执行   1:发生错误


    private String message;//信息
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Result() {
        this.code=0;
        this.message = "执行成功";
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}