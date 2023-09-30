package com.tienda.mapper;

import com.tienda.Model.Role;
import com.tienda.Model.User;
import com.tienda.dto.UserDTO;

public class UserMapper {
    public static UserDTO userToUserDTO(User user) {
        if (user == null) {
            return null; // Si el usuario es nulo, devuelve un UserDTO nulo.
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserid());
        userDTO.setUserName(user.getUserName() != null ? user.getUserName() : null);
        userDTO.setUserLastName(user.getUserLastName() != null ? user.getUserLastName() : null);
        userDTO.setUserDni(user.getUserDni() != null ? user.getUserDni() : null);
        userDTO.setUserEmail(user.getUserEmail()!= null ? user.getUserEmail() : null);
        userDTO.setUserPassword(user.getUserPassword() != null ? user.getUserPassword() : null);
        userDTO.setRoleId(user.getRole() != null ? user.getRole().getRoleId(): null);
        userDTO.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
        return userDTO;
    }

    public static User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserid(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setUserLastName(userDTO.getUserLastName());
        user.setUserDni(userDTO.getUserDni());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(userDTO.getUserPassword());

        // Si el campo roleId no es nulo en UserDTO, crea un nuevo objeto Role y asígnalo a User.
        if (userDTO.getRoleId() != null) {
            user.setRole(new Role(userDTO.getRoleId()));
        }

        // Puedes manejar la conversión inversa si es necesario
        return user;
    }
}
