package ru.web.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.web.boot.model.Role;
import ru.web.boot.model.User;
import ru.web.boot.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    public AdminController() {
    }

    @GetMapping("")
    public String userList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("")
    public String deleteUser(@RequestParam(required = true, defaultValue = "") Long userId,
                             @RequestParam(required = true, defaultValue = "") String action,
                             Model model) {
        if (action.equals("delete")) {
            userService.deleteUserById(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        boolean ifHaveAdmin = false;
        boolean ifHaveUser = false;
        model.addAttribute("ifHaveAdmin", ifHaveAdmin);
        model.addAttribute("ifHaveUser", ifHaveUser);
        return "addUser";
    }

    @PostMapping("addUser")
    public String saveUserFromForm(@ModelAttribute("user") User user,
                                   @RequestParam(value = "ifHaveAdmin", required = false) boolean adminRole,
                                   @RequestParam(value = "ifHaveUser", required = false) boolean userRole) {
        user.setRoles(setRolesInController(adminRole, userRole));
        userService.saveUser(user);
        ;
        return "redirect:allUsers";
    }

    @GetMapping("delete/{id}")
    public String deleteUserController(@PathVariable long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("edit/{id}")
    public String editUserController(@PathVariable long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        boolean ifHaveAdmin = false;
        boolean ifHaveUser = false;
        for (Role role : user.getRoles()) {
            if (role.getName().equals("ROLE_ADMIN")) {
                ifHaveAdmin = true;
            }
            if (role.getName().equals("ROLE_USER")) {
                ifHaveUser = true;
            }
        }
        model.addAttribute("ifHaveAdmin", ifHaveAdmin);
        model.addAttribute("ifHaveUser", ifHaveUser);
        return "editUser";
    }

    @PostMapping("edit/{id}")
    public String editUserController(@ModelAttribute("user") User user,
                                     @RequestParam(value = "ifHaveAdmin", required = false) boolean adminRole,
                                     @RequestParam(value = "ifHaveUser", required = false) boolean userRole) {
        user.setRoles(setRolesInController(adminRole, userRole));
        userService.updateUser(user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("allUsers")
    public String listController(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "allUsers";
    }

    public Set<Role> setRolesInController(boolean adminRole, boolean userRole) {
        Set<Role> roles = new HashSet<>();
        if (adminRole) {
            roles.add(new Role(1L, "ROLE_ADMIN"));
        }
        if (userRole) {
            roles.add(new Role(2L, "ROLE_USER"));
        }
        return roles;
    }
}
