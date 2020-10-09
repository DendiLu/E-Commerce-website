package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.service.goods.SkuSearchService;
import com.qingcheng.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class SearchController {
    @Reference
    SkuSearchService skuSearchService;

    @GetMapping("/search")
    public String search(Model model,@RequestParam Map<String, String> searchMap) throws Exception {

        searchMap = WebUtil.convertCharsetToUTF8(searchMap);
        Map result = skuSearchService.search2(searchMap);
        model.addAttribute("result",result);

        //url处理
        StringBuffer stringBuffer = new StringBuffer("/search.do?");

        for(Map.Entry entry:searchMap.entrySet()){
            stringBuffer.append("&"+entry.getKey()+"="+entry.getValue());
        }
        model.addAttribute("url",stringBuffer.toString());
        model.addAttribute("searchMap",searchMap);
        return "search2";
    }
}
