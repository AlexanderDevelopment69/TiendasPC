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
    private Long userId;
    private String userName;
    private String userLastName;
    private String userDni;
    private String userEmail;
    private String userPassword;

    //Role
    private Long roleId;
    private String roleName;



// Constructor, getters y setters


}

