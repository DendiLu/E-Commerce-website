package com.qingcheng.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.system.Admin;
import com.qingcheng.pojo.system.AdminRoleIds;
import com.qingcheng.service.system.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Reference
    private AdminService adminService;


    @GetMapping("/findAll")
    public List<Admin> findAll(){
        return adminService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Admin> findPage(int page, int size){
        return adminService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Admin> findList(@RequestBody Map<String,Object> searchMap){
        return adminService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Admin> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  adminService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Admin findById(Integer id){
        return adminService.findById(id);
    }


    @PostMapping("/addAdmin")
    @PreAuthorize("hasAuthority('config')")
    public Result add(@RequestBody AdminRoleIds adminRoleIds){
        Admin admin = adminRoleIds.getAdmin();
        List<Integer> roleIds = adminRoleIds.getRoleIds();
        adminService.add(admin,roleIds);
        return new Result();
    }

    @GetMapping("/updateAdmin")
    @PreAuthorize("hasAnyAuthority()")
    public Result updateAdmin(String name,String password){
        String currName = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("name: "+name+", password: "+password);
        if (name!=null&&!"".equals(name)){
            adminService.updateName(currName,name);
            return new Result(0,"成功修改用户名");
        }else if(password!=null&&!"".equals(password)){
            adminService.updatePassword(currName,password);
            return new Result(0,"成功修改密码");
        }else {
            return new Result(1,"未能成功修改，请检查你的输入");
        }
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        adminService.delete(id);
        return new Result();
    }

}
