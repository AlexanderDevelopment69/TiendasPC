package com.tienda.ControllerGUI.User;


import com.tienda.Model.Role;

public class UserSession {
    private static UserSession instance;
    private Long userId;
    private String dni;
    private String name;
    private String lastName;
    private String email;

    private Long roleId;
    private String roleName;

    private UserSession() {

    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(Long userId, String name, String lastName, String email, String dni,Long roleId, String roleName) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.dni = dni;
        this.roleId=roleId;
        this.roleName = roleName;
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


    public String getDni() {
        return dni;
    }

    public String getRoleName() {
        return roleName;
    }


    public Long getRoleId() {
        return roleId;
    }

    public void clearSession() {
        userId = null;
        name = null;
        lastName = null;
        email = null;
        dni = null;
        roleId=null;
        roleName = null;
    }

    public boolean isLoggedIn() {
        return userId != null;
    }
}
