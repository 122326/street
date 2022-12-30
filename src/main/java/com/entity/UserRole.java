package com.entity;

public class UserRole {
    private long LocalAuthId;
    private long roleId;

    public UserRole(long localAuthId, long roleId) {
        LocalAuthId = localAuthId;
        this.roleId = roleId;
    }

    public long getLocalAuthId() {
        return LocalAuthId;
    }

    public void setLocalAuthId(long localAuthId) {
        LocalAuthId = localAuthId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRoles{" +
                "LocalAuthId=" + LocalAuthId +
                ", roleId=" + roleId +
                '}';
    }
}
