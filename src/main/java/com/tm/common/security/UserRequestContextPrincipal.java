package com.tm.common.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestContextPrincipal {
    private Long id;
    private String userName;
    private String email;
    private Boolean hasSuperSystemRole;
    private List<PrivilegeModel> privileges = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<PrivilegeModel> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeModel> privileges) {
        this.privileges = privileges;
    }
}
