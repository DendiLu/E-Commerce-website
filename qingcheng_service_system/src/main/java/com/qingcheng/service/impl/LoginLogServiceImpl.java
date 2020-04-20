package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.LoginLogMapper;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.LoginLog;
import com.qingcheng.service.system.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    LoginLogMapper loginLogMapper;

    @Override
    public List<LoginLog> findAll() {
        return loginLogMapper.selectAll();
    }

    @Override
    public PageResult<LoginLog> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<LoginLog> loginLogPages = (Page<LoginLog>) loginLogMapper.selectAll();
        return new PageResult<>(loginLogPages.getTotal(), loginLogPages.getResult());
    }

    @Override
    public List<LoginLog> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return loginLogMapper.selectByExample(example);
    }

    @Override
    public PageResult<LoginLog> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<LoginLog> loginLogPages = (Page<LoginLog>) loginLogMapper.selectByExample(example);
        return new PageResult<>(loginLogPages.getTotal(), loginLogPages.getResult());
    }

    @Override
    public LoginLog findById(Integer id) {
        return loginLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(LoginLog loginLog) {
        loginLogMapper.insertSelective(loginLog);
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(LoginLog.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 用户名
            if (searchMap.get("loginName") != null && !"".equals(searchMap.get("loginName"))) {
                criteria.andEqualTo("loginName", searchMap.get("loginName"));
            }
            //
            if (searchMap.get("ip") != null && !"".equals(searchMap.get("ip"))) {
                criteria.andEqualTo("ip", searchMap.get("ip"));
            }
            //
            if (searchMap.get("loginTime") != null && !"".equals(searchMap.get("loginTime"))) {
                criteria.andLike("loginTime", "%"+searchMap.get("loginTime")+"%");
            }

            if (searchMap.get("browserName") != null && !"".equals(searchMap.get("browserName"))) {
                criteria.andLike("browserName", "%"+searchMap.get("browserName")+"%");
            }

            if (searchMap.get("location") != null && !"".equals(searchMap.get("location"))) {
                criteria.andLike("location", "%"+searchMap.get("location")+"%");
            }

            // id
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }

        }
        return example;
    }
}
