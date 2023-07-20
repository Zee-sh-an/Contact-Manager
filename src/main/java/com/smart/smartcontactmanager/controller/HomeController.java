package com.smart.smartcontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/home")
	public String handler(Model model) {

		model.addAttribute("title", "Home - smart contact manager");

		return "home";
	}

	@GetMapping("/about")
	public String aboutHandler(Model model) {

		model.addAttribute("title", "About - smart contact manager");

		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "Register - smart contact manager");
		model.addAttribute("user", new User());

		return "signup";
	}

//	This handler for registering user  
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,

			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try

		{

			if (!agreement) {
				System.out.println("You have not agreed terms and conditions");
				throw new Exception("You have not agreed terms and conditions");
			}

			if (result1.hasErrors()) {
				System.out.println("bindingResult");
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement " + agreement);
			System.out.println("User " + user);

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registerd!!", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!" + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	
//	handler for login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		
		model.addAttribute("title", "Login Page");
		
		return"login";
	}
	
	@GetMapping("/a")
	public String adt() {
		return"a";
	}
}
