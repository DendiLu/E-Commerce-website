package com.qingcheng.dao;

import com.qingcheng.pojo.order.CategoryReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.rmi.MarshalledObject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CategoryReportMapper extends Mapper<CategoryReport> {
    @Select("select oi.category_id1 categoryId1,oi.category_id2 categoryId2,oi.category_id3 categoryId3," +
            "       date_format(o.pay_time,'%Y-%m-%d')  countDate," +
            "       sum(oi.num) num,SUM(oi.pay_money) money " +
            "from tb_order_item oi, tb_order o " +
            "where oi.order_id=o.id and o.pay_status='1' and o.is_delete='0' " +
            "  and date_format(o.pay_time,'%Y-%m-%d')='${date}' " +
            "group by oi.category_id1,oi.category_id2,oi.category_id3,date_format(o.pay_time,'%Y-%m-%d')")
    public List<CategoryReport> categroyReport(@Param("date")LocalDate date);

    @Select("SELECT category_id1 categoryId1, c.name categoryName, SUM(num) num, SUM(money), SUM(money) money " +
            "from tb_category_report r,v_category1 c " +
            "where r.category_id1=c.id and count_date>=#{date1} and count_date<=#{date2} " +
            "group by category_id1")
    public List<Map> category1Count(@Param("date1") String date1,@Param("date2") String date2);
}
