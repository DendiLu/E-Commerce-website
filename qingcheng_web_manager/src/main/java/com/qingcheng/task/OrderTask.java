package com.qingcheng.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.service.order.CategoryReportService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Lazy(false)
public class OrderTask {
    public OrderTask(){
        System.out.println("OrderTask类创建了");
    }
    @Reference
    private CategoryReportService categoryReportService;

    @Scheduled(cron = "* * * * * ?")
    public void test(){
        System.out.println(new Date());
    }

    @Scheduled(cron = "0 * * * * ?")
    public void createCategoryReportData(){
        System.out.println("生成类目统计数据");
        categoryReportService.createData();
    }
}
