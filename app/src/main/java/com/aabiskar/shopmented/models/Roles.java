package com.aabiskar.shopmented.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Roles
{
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("role")
    @Expose
    private String role;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
