package com.tienda.dto;


import com.tienda.Model.Role;
import com.tienda.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;

import javax.persistence.Column;


@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;

    private String name;

    private String lastName;

    private String dni;

    private String email;

    private String password;


    //Role

    private Long roleId;

    private String roleName;

    public UserDTO(Long id, String name, String lastName, String dni, String email, String password, String roleName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.roleName = roleName;
    }

    public UserDTO(String email, String password, String dni, String lastNames, String names) {
        this.email=email;
        this.password=password;
        this.dni=dni;
        this.lastName=lastNames;
        this.name=names;
    }



// Constructor, getters y setters


}

