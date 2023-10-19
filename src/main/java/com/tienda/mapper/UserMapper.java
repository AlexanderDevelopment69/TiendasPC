package com.tienda.mapper;

import com.tienda.Model.Role;
import com.tienda.Model.User;
import com.tienda.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    /**
     * Convierte una entidad User en un DTO UserDTO.
     *
     * @param user La entidad User a ser convertida.
     * @return El DTO UserDTO resultante.
     */
    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserLastName(user.getUserLastName());
        userDTO.setUserDni(user.getUserDni());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserPassword(user.getUserPassword());

        if (user.getRole() != null) {
            userDTO.setRoleId(user.getRole().getRoleId()); // Mapea el roleId del rol asociado si existe
        }

        if (user.getRole() != null) {
            userDTO.setRoleName(user.getRole().getRoleName()); // Mapea el roleId del rol asociado si existe
        }



        // Aquí puedes manejar la asignación de la propiedad relacionada, como el rol del usuario

        return userDTO;
    }

    /**
     * Convierte un DTO UserDTO en una entidad User.
     *
     * @param userDTO El DTO UserDTO a ser convertido.
     * @return La entidad User resultante.
     */
    public static User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setUserLastName(userDTO.getUserLastName());
        user.setUserDni(userDTO.getUserDni());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(userDTO.getUserPassword());

        // Asigna el roleId del UserDTO al usuario
        if (userDTO.getRoleId() != null) {
            Role role = new Role();
            role.setRoleId(userDTO.getRoleId());
            user.setRole(role);
        }


        // Puedes manejar la asignación de las propiedades relacionadas aquí, como el rol del usuario

        return user;
    }

    /**
     * Convierte una lista de entidades User en una lista de DTOs UserDTO.
     *
     * @param users La lista de entidades User a ser convertida.
     * @return La lista de DTOs UserDTO resultante.
     */
    public static List<UserDTO> toUserDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }


}
