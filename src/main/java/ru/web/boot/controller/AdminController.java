package ru.web.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.web.boot.model.Role;
import ru.web.boot.model.User;
import ru.web.boot.service.UserService;

import java.security.Principal;
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
    public String getAdminPage(Model model, Principal principal) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping("addUser")
    public String saveUserFromForm(@ModelAttribute("user") User user,
                                    @RequestParam(value = "checkRoles", required = false) String userRole){
        user.setRoles(setRolesInController(userRole));
        userService.saveUser(user);
        return "redirect:";
    }

    @PostMapping("delete/{id}")
    public String deleteUserController(@PathVariable long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/";
    }

    @PostMapping("edit/{id}")
    public String editUserController(@PathVariable long id,
                                     @ModelAttribute("newUser") User user,
                                     @RequestParam(value = "editFirstname"  ,  required = false) String firstname    ,
                                     @RequestParam(value = "editLastname"   ,  required = false) String lastname    ,
                                     @RequestParam(value = "editEmail"      ,  required = false) String email       ,
                                     @RequestParam(value = "editPassword"   ,  required = false) String password    ,
                                     @RequestParam(value = "checkRoles"     ,  required = false) String checkRoles){
        user.setRoles(setRolesInController(checkRoles));
        user.setId(id);
        System.out.println("**********"+firstname);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        if (password.isEmpty()){
            user.setPassword(userService.getUserById(id).getPassword());
        } else {
            user.setPassword(password);
        }
        userService.updateUser(user);
        return "redirect:/admin/";
    }

    public Set<Role> setRolesInController(String checkRoles) {
        Set<Role> roles = new HashSet<>();
        if (checkRoles != null){
            if (checkRoles.contains("ADMIN")) {
                roles.add(new Role(1L, "ROLE_ADMIN"));
            }
            if (checkRoles.contains("USER")) {
                roles.add(new Role(2L, "ROLE_USER"));
            }
        }
        return roles;
    }
}
