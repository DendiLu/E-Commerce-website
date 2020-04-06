package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;
import com.qingcheng.service.goods.BrandService;
import org.graalvm.compiler.asm.sparc.SPARCAssembler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;
    @RequestMapping("/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }
    @GetMapping("/findPage")
    public PageResult<Brand> findPage(int page, int size){
        return  brandService.findPage(page, size);
    }
    @PostMapping("/findList")  //接受POST请求体中的json数据
    public List<Brand> findList(@RequestBody Map searchMap){
        return brandService.findList(searchMap);
    }
}
