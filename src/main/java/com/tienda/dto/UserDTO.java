package com.tienda.dto;


import com.tienda.Model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String userName;
    private String userLastName;
    private String userDni;
    private String userEmail;
    private String userPassword;
    private Role role;

    //Role
    private Long roleId;
    private String roleName;



// Constructor, getters y setters


}

