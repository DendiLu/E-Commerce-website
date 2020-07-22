package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.business.Ad;
import com.qingcheng.service.business.AdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Reference
    private AdService adService;

    @GetMapping("/index")
    public String index(Model model){
        List<Ad> lbtList = adService.findByPosition("web_index_lb"); //首页的轮播图广告
        model.addAttribute("lbt",lbtList);
        return "index";
    }

}
