package com.tienda.ControllerGUI.User;


public class UserSession {
    private static UserSession instance;
    private Long userId;
    private String dni;
    private String name;
    private String lastName;
    private String email;
    private String roleName;

    private UserSession() {

    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(Long userId, String name, String lastName, String email, String dni, String roleName) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.dni = dni;
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

    public void clearSession() {
        userId = null;
        name = null;
        lastName = null;
        email = null;
        dni = null;
        roleName = null;
    }

    public boolean isLoggedIn() {
        return userId != null;
    }
}
