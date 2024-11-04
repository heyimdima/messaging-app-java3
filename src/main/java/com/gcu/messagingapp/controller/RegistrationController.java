package com.gcu.messagingapp.controller;

import com.gcu.messagingapp.model.User;
import com.gcu.messagingapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for UserRepository and PasswordEncoder
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET request to show the registration form
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty User object to the model
        model.addAttribute("pageTitle", "Register"); // Set page title
        return "register"; // Render the registration page
    }

    // POST request to process the registration form submission
    @PostMapping
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Encode the password using BCryptPasswordEncoder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user in the database
        userRepository.save(user);

        // Redirect to the login page after successful registration
        return "redirect:/login";
    }
}