package com.tienda.DAO;

import com.tienda.Model.User;

import java.util.List;

public interface UserDao {

    User getUserByEmail(String email);
    Boolean authenticateUser(String email, String password);
    void addUser(User user);
    User getUserById(Long userId);
    List<User> getAllUsers();

    boolean isDniExists(String dni);

    boolean isEmailExists(String email);

}
