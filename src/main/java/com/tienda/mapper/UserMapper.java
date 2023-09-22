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
        userDTO.setId(user.getId());
        userDTO.setName(user.getName() != null ? user.getName() : null);
        userDTO.setLastName(user.getLastName() != null ? user.getLastName() : null);
        userDTO.setDni(user.getDni() != null ? user.getDni() : null);
        userDTO.setEmail(user.getEmail() != null ? user.getEmail() : null);
        userDTO.setPassword(user.getPassword() != null ? user.getPassword() : null);
        userDTO.setRoleId(user.getRole() != null ? user.getRole().getId() : null);
        userDTO.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
        return userDTO;
    }

    public static User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setDni(userDTO.getDni());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        // Si el campo roleId no es nulo en UserDTO, crea un nuevo objeto Role y asígnalo a User.
        if (userDTO.getRoleId() != null) {
            user.setRole(new Role(userDTO.getRoleId()));
        }

        // Puedes manejar la conversión inversa si es necesario
        return user;
    }
}
