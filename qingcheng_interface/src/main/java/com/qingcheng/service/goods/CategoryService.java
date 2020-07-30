package com.qingcheng.service.goods;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Category;

import java.util.*;

/**
 * category业务逻辑层
 */
public interface CategoryService {
    /**
     * 查找前台首页导航条内容，有一级、二级、三级分类
     * @return
     */
    public List<Map> findCategoryTree();

    /**
     * 将首页树状导航栏条目存进redis
     */
    public void saveCategoryTreeToRedis();

    public List<Category> findAll();


    public PageResult<Category> findPage(int page, int size);


    public List<Category> findList(Map<String,Object> searchMap);


    public PageResult<Category> findPage(Map<String,Object> searchMap,int page, int size);


    public Category findById(Integer id);

    public void add(Category category);


    public void update(Category category);


    public void delete(Integer id);

}
