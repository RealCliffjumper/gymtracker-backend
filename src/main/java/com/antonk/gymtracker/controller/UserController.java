package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
class UserController {

    private UserService userService;

    @PostMapping(path = "/registration")
    public ResponseEntity<User> signUp(@RequestBody SignUpDto signUpDto) {
        User user = userService.signUpUser(signUpDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginDto loginDto) {
        User user = userService.loginUser(loginDto);
        return ResponseEntity.ok(user);
    }
}
