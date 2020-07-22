package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.dao.AdminMapper;
import com.qingcheng.dao.ResourceMapper;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.Resource;
import com.qingcheng.service.system.AdminRoleService;
import com.qingcheng.service.system.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Service(interfaceClass = ResourceService.class, protocol = "dubbo")
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private AdminMapper adminMapper;
    /**
     * 根据根据登录名查询资源KEY列表
     * @param adminName
     * @return
     */
    @Override
    public List<String> getResources(String adminName) {
        List<Resource> resources = resourceMapper.getResources(adminName);
        if (resources==null)
            return null;
        List<String> resourceKeys = new ArrayList<>();
        for (Resource r :
                resources) {
            resourceKeys.add(r.getResKey());
        }
        return resourceKeys;
    }
}
