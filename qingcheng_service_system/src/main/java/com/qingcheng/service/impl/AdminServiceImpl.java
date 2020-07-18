package com.qingcheng.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qingcheng.dao.AdminMapper;
import com.qingcheng.dao.AdminRoleMapper;
import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.AdminRole;
import com.qingcheng.service.system.AdminService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = AdminService.class, protocol = "dubbo")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Admin> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectAll();
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        Map map = new HashMap();
        map.put("loginName",username);
        Example example = createExample(map);
        List<Admin> admins = adminMapper.selectByExample(example);
        if(admins.size()==0){
            throw new RuntimeException("根据用户名查询为空");
        }else {
            Admin admin = admins.get(0);
            String gensalt = BCrypt.gensalt();
            String hashpw = BCrypt.hashpw(newPassword, gensalt);
            admin.setPassword(hashpw);
            adminMapper.updateByPrimaryKeySelective(admin);
        }
    }

    @Override
    public void updateName(String oldName, String newName) {
        Map map = new HashMap();
        map.put("loginName",oldName);
        Example example = createExample(map);
        List<Admin> admins = adminMapper.selectByExample(example);
        if(admins.size()==0){
            throw new RuntimeException("根据用户名查询为空");
        }else {
            Admin admin = admins.get(0);
            admin.setLoginName(newName);
            adminMapper.updateByPrimaryKeySelective(admin);
        }
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Admin> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return adminMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Admin> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 将多个权限添加给管理员，添加进tb_admin_role表
     * @param admin 管理员
     * @param roleIds 复数个权限id
     */
    @Transactional
    public void add(Admin admin, List<Integer> roleIds) {
        Integer adminId = admin.getId();
        String gensalt = BCrypt.gensalt();
        String hashpw = BCrypt.hashpw(admin.getPassword(), gensalt);
        admin.setPassword(hashpw);
        adminMapper.insert(admin);
        int i = admin.getId();
        for(Integer id: roleIds){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(i);
            adminRole.setRoleId(id);
            adminRoleMapper.insert(adminRole);
        }
    }

    /**
     * 修改
     * @param admin
     */
    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("loginName")!=null && !"".equals(searchMap.get("loginName"))){
                criteria.andEqualTo("loginName",searchMap.get("loginName"));
            }
            // 密码
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                criteria.andEqualTo("password",searchMap.get("password"));
            }
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
            }

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }

}
