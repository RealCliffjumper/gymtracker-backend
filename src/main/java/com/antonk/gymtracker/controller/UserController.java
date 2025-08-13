package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.JWT.JWTTokenProvider;
import com.antonk.gymtracker.dto.JWTDto;
import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
class UserController {

    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping(path = "auth/registration")
    public ResponseEntity<User> signUpUser(@RequestBody SignUpDto signUpDto) {
        User user = userService.signUpUser(signUpDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "auth/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        User user = userService.loginUser(loginDto);
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JWTDto(user, token));
    }
}
