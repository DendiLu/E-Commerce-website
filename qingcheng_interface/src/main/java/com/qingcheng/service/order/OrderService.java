package com.qingcheng.service.order;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.order.Order;

import java.util.*;

/**
 * order业务逻辑层
 */
public interface OrderService {

    public List<Order> findOrders(String[] ids);

    public List<Order> findAll();


    public PageResult<Order> findPage(int page, int size);


    public List<Order> findList(Map<String,Object> searchMap);


    public PageResult<Order> findPage(Map<String,Object> searchMap,int page, int size);


    public Order findById(String id);

    public void add(Order order);


    public void update(Order order);


    public void delete(String id);

    public int updateBatch(List<Order> orders);

    /*
        定时任务自动处理超时订单
     */
    public void handleOrderTimeout();

}