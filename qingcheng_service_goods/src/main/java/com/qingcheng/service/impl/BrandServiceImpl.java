package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.BrandMapper;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.goods.Brand;
import com.qingcheng.service.goods.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public PageResult<Brand> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Brand> pageResult = (Page<Brand>) brandMapper.selectAll();
        return new PageResult<>(pageResult.getTotal(),pageResult.getResult());
    }

    @Override
    public List<Brand> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return brandMapper.selectByExample(example);
    }

    @Override
    public PageResult<Brand> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Brand> brandPage = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<>(brandPage.getTotal(),brandPage.getResult());
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }


    private Example createExample(Map<String, Object> searchMap){
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            if(searchMap.get("name")!=null&&!"".equals(searchMap.get("name"))){
                criteria.andLike("name",(String)searchMap.get("name")+"%");
            }
            if(searchMap.get("letter")!=null&&!"".equals(searchMap.get("letter"))){
                criteria.andLike("letter",(String)searchMap.get("letter"));
            }

        }
        return example;
    }
}
