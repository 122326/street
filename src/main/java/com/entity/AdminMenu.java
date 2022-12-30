package com.entity;

public class AdminMenu {
    long roleId;
    String murl;
    String mname;

    public AdminMenu(long roleId, String murl, String mname) {
        this.roleId = roleId;
        this.murl = murl;
        this.mname = mname;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    @Override
    public String toString() {
        return "AdminMenu{" +
                "roleId=" + roleId +
                ", murl='" + murl + '\'' +
                ", mname='" + mname + '\'' +
                '}';
    }
}
