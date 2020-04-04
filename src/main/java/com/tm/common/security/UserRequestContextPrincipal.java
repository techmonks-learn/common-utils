package com.tm.common.security;

import java.util.ArrayList;
import java.util.List;

public class UserRequestContextPrincipal {
    private Long userId;
    private String userName;
    private String email;
    private Boolean hasSuperSystemRole;
    private List<PrivilegeModel> privilegeModels = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHasSuperSystemRole() {
        return hasSuperSystemRole;
    }

    public void setHasSuperSystemRole(Boolean hasSuperSystemRole) {
        this.hasSuperSystemRole = hasSuperSystemRole;
    }

    public List<PrivilegeModel> getPrivilegeModels() {
        return privilegeModels;
    }

    public void setPrivilegeModels(List<PrivilegeModel> privilegeModels) {
        this.privilegeModels = privilegeModels;
    }
}
