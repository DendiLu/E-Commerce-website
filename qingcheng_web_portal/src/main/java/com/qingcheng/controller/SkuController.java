package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.service.goods.SkuService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sku")
@CrossOrigin  //支持跨域，可单独注解方法
public class SkuController {

    @Reference
    private SkuService skuService;

    @GetMapping("/price")
    public Integer getPrice(String sku){
        return skuService.findPrice(sku);
    }
}
