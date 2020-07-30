package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.business.Ad;
import com.qingcheng.service.business.AdService;
import com.qingcheng.service.goods.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Reference
    private AdService adService;

    @Reference
    private CategoryService categoryService;

    @GetMapping("/index")
    public String index(Model model){
        List<Ad> lbtList = adService.findByPosition("web_index_lb"); //首页的轮播图广告
        model.addAttribute("lbt",lbtList);

        List<Map> categoryList = categoryService.findCategoryTree();  //首页分类导航
        model.addAttribute("categoryList",categoryList);
        return "index";
    }

}
