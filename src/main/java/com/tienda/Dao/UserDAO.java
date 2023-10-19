package com.tienda.Dao;

import com.tienda.dto.UserDTO;

import java.util.List;

public interface UserDAO {
    void saveUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO);
    void deleteUser(Long userId);
    List<UserDTO> getAllUsers();
    
    UserDTO getUserByEmail(String email);
    UserDTO getUserByDni(String dni);
    UserDTO getUserById(Long userId);
    boolean authenticateUser(String email, String password);
    boolean isDniExists(String dni);
    boolean isEmailExists(String email);
    public void assignUserRole(Long userId, Long roleId);
    List<UserDTO> searchUsersBySingleCriteria(String criteria);

}
