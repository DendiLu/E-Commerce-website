package com.qingcheng.service.goods;

import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    public List<Brand> findAll();
    public PageResult<Brand> findPage(int page, int size);
    public List<Brand> findList(Map<String, Object> searchMap);
}
