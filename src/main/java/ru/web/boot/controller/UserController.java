package ru.web.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.web.boot.model.User;
import ru.web.boot.service.UserService;

import java.security.Principal;

@Controller
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	public UserController() {
	}

	@GetMapping("/user")
	public String userHomePage(Model model, Principal principal) {
		User user = (User) userService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
		return "helloUser";
	}

	@GetMapping("/")
	public String myHome(Model model) {
		return "home";
	}



}