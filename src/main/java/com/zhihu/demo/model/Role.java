package com.zhihu.demo.model;

/**
 * @author 邓超
 * @description 角色model
 * @create 2018/9/13
 */
public class Role {
    private int roleId;
    private String roleName;
    private String permission;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
