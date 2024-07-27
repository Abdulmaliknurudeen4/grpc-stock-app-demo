package com.nexusforge.aggregator.controller;

import com.nexusforge.aggregator.service.UserService;
import com.nexusforge.user.UserInformation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "userId", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInformation getUserInformation(@PathVariable Integer userId) {
        return this.userService.getUserInformation(userId);
    }
}
