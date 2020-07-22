package com.qingcheng.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.dao.AdminRoleMapper;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.AdminRole;
import com.qingcheng.pojo.system.AdminRoleIds;
import com.qingcheng.service.system.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
@Service(interfaceClass = AdminRoleService.class, protocol = "dubbo")
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    AdminRoleMapper adminRoleMapper;

    /**
     * 根据管理员id查找其角色，并以组合实体类返回
     * @param adminId 管理员id
     * @return 组合实体类
     */
    @Override
    public AdminRoleIds findById(Integer adminId) throws Exception{

        Example example = createExample(adminId);
        List<AdminRole> adminRoles = adminRoleMapper.selectByExample(example);
        if (adminRoles==null||adminRoles.size()==0){
            throw new Exception("根据此管理员id没有找到任何角色信息");
        }else {
            Admin admin = new Admin();
            admin.setId(adminId);
            List<Integer> roleIds = new ArrayList<>();
            for (AdminRole ar : adminRoles){
                roleIds.add(ar.getRoleId());
            }
            AdminRoleIds adminRoleIds = new AdminRoleIds(admin,roleIds);
            return adminRoleIds;
        }
    }

    /**
     * 更新管理员账户的角色，先清空该账户的角色信息
     * @param adminId
     * @param roleIds
     * @throws Exception
     */
    @Override
    @Transactional
    public void update(Integer adminId,List<Integer> roleIds) throws Exception {
        Example example = createExample(adminId);
        adminRoleMapper.deleteByExample(example);
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(adminId);
        for(Integer roleId : roleIds){
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }
    }


    private Example createExample(Integer adminId){
        Example example = new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId", adminId);
        return example;
    }
}
