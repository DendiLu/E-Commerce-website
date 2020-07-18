package com.qingcheng.pojo.system;

import java.io.Serializable;
import java.util.List;

public class AdminRoleIds implements Serializable {
    private Admin admin;
    private List<Integer> roleIds;

    @Override
    public String toString() {
        return "AdminRoleIds{" +
                "admin=" + admin +
                ", roleIds=" + roleIds +
                '}';
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
