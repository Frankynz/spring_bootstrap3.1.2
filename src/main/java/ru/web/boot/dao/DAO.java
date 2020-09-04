package ru.web.boot.dao;


import ru.web.boot.model.User;

import java.util.List;

public interface DAO {

    User getUserById(long id);

    List<User> getAllUser();

    void deleteUserById(long id);

    void updateUser(User user);

    void saveUser(User user);

    User findUserByUsername(String username);

}
