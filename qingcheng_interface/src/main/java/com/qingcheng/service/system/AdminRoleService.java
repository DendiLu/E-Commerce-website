package com.qingcheng.service.system;

import com.qingcheng.pojo.system.AdminRoleIds;

import java.util.List;

public interface AdminRoleService {
    public AdminRoleIds findById(Integer adminId) throws Exception;

    public void update(Integer adminId,List<Integer> roleIds) throws Exception;
}
