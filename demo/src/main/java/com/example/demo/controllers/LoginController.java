package com.example.demo.controllers;

import com.example.demo.Entities.User;
import com.example.demo.Repository.UserRepository;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Validated @RequestBody User user) {

        Optional<User> result = userRepository.findByEmail(user.getEmail());
        if (result.isPresent()) {
            return null;

        } else {
            return "error";
        }
    }

}
