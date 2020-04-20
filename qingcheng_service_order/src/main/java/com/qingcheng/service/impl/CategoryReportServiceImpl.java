package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.dao.CategoryReportMapper;
import com.qingcheng.pojo.order.CategoryReport;
import com.qingcheng.service.order.CategoryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CategoryReportServiceImpl implements CategoryReportService {
    @Autowired
    private CategoryReportMapper categoryReportMapper;

    /**
     * 从report表中查询数据
     * @param date
     * @return
     */
    @Override
    public List<CategoryReport> categroyReport(LocalDate date) {
        return categoryReportMapper.categroyReport(date);
    }

    /**
     * 连接两表查询report数据并插入report表中
     */
    @Override
    public void createData() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        List<CategoryReport> categoryReports = categoryReportMapper.categroyReport(localDate);
        for(CategoryReport categoryReport:categoryReports){
            System.out.println(categoryReport);
            categoryReportMapper.insert(categoryReport);
        }
    }

    /**
     * 手动连接两表查询report数据并插入report表中
     * @param localDate
     */
    @Override
    public void createData1(LocalDate localDate) {
        List<CategoryReport> categoryReports = categoryReportMapper.categroyReport(localDate);
        for(CategoryReport categoryReport:categoryReports){
            System.out.println(categoryReport);
            categoryReportMapper.insert(categoryReport);
        }
    }

   @Override
    public List<Map> category1Count(String date1, String date2) {
        return categoryReportMapper.category1Count(date1,date2);
    }
}
