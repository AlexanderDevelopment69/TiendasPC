package com.tienda.ControllerGUI.User;

import com.tienda.Model.Role;

import java.util.Set;

public class UserSession {
    private static UserSession instance;
    private Long userId;
    private String name;

    private String lastName;
    private String email;
    private String role;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(Long userId, String name,String lastName,String email, String  role) {
        this.userId = userId;
        this.name = name;
        this.lastName=lastName;
        this.email=email;
        this.role = role;
    }


    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void clearSession() {
        userId = null;
        name = null;
        lastName=null;
        email=null;
        role = null;
    }

    public boolean isLoggedIn() {
        return userId != null;
    }
}
