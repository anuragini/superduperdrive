package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserServiceManager;
import com.udacity.jwdnd.course1.cloudstorage.services.UserServiceManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserServiceManager userServiceManager;

    public SignupController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    //Displays the Signup Page
    @GetMapping
    public String signupView() {
        return "signup";
    }

    //Handles user signup  requests

    @PostMapping
    public String signupUser(@ModelAttribute User user, Model model) {
        String signupError = null;

        if (!userServiceManager.isUsernameAvailable(user.getUsername())) {
            signupError = "The username already exists.";
        }

        if (signupError == null) {
            int rowsAdded = userServiceManager.createUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }

    //This function validatesUser will validate user details for signup
    //wll return an error message if this validation fails
    private String validateUser(User user) {
        if (!userServiceManager.isUsernameAvailable(user.getUsername())) {
            ;
            return "This username already exists";
        }
        return null;
    }
}

