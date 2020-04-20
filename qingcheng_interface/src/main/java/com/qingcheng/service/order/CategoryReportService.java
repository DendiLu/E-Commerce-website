package com.qingcheng.service.order;

import com.qingcheng.pojo.order.CategoryReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CategoryReportService {

    public List<CategoryReport> categroyReport(LocalDate date);
    //自动生成昨天的报告
    public void createData();

    //手动生成报告
    public void createData1(LocalDate date);

    public List<Map> category1Count(String date1, String date2);



}
