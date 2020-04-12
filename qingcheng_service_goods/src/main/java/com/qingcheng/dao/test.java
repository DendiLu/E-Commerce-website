package com.qingcheng.dao;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    @Test
    public void test(){
        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));
    }
}
