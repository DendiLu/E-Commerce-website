package com.qingcheng.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.order.CategoryReport;
import com.qingcheng.service.order.CategoryReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoryReport")
public class CategoryReportController {

    @Reference
    private CategoryReportService categoryReportService;

    /**
     * 统计昨天的数据（商品类目)
     * @return
     */
    @GetMapping("/yesterday")
    public List<CategoryReport> yesterday(){
        LocalDate localDate = LocalDate.now().minusDays(1);
        return categoryReportService.categroyReport(localDate);
    }

    @GetMapping("/category1Count")
    public List<Map> category1Count(String date1,String date2){
        return categoryReportService.category1Count(date1,date2);
    }

    @GetMapping("/generateReport")
    public List<Map> generateReport(String date){

        LocalDate localDate =  LocalDate.of(2020,4,17);
         categoryReportService.createData1(localDate);
         return categoryReportService.category1Count(date,date);
    }

}
