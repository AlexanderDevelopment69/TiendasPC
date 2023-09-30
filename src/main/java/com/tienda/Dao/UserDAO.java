package com.tienda.Dao;

import com.tienda.dto.UserDTO;

import java.util.List;

public interface UserDAO {
    void addUser(UserDTO userDTO);
    void updateUser(Long userId, UserDTO updatedUserDTO);
    void updateUserRole(Long userId, Long newRoleId);
    void deleteUserById(Long userId);

    List<UserDTO> getAllUsers();
    
    UserDTO getUserByEmail(String email);
    UserDTO getUserByDni(String dni);
    UserDTO getUserById(Long userId);
    Boolean authenticateUser(String email, String password);
    boolean isDniExists(String dni);

    boolean isEmailExists(String email);





}
