package ru.web.boot.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.web.boot.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUserById(Long id);

    void saveUser(User user);

    void deleteUserById(Long id);

    void updateUser(User user);
}
