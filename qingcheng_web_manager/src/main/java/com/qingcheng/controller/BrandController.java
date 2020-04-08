package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
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
        //新改动test
        return brandService.findList(searchMap);
    }
    @PostMapping("/findPage")
    public PageResult<Brand> findPage(@RequestBody Map searchMap,int page, int size){
        return brandService.findPage(searchMap,page,size);
    }
    @GetMapping("/findById")
    public Brand findById(Integer id){
        return brandService.findById(id);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result();
    }
    @PostMapping("update")
    public Result update(@RequestBody Brand brand){
        brandService.update(brand);
        return new Result();
    }
    @GetMapping("/delete")
    public Result delete(Integer id){
        brandService.delete(id);
        return new Result();
    }
}
