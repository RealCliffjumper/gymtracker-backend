package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.JWT.JWTTokenProvider;
import com.antonk.gymtracker.dto.*;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api")
@AllArgsConstructor
class UserController {

    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping(path = "auth/registration")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpDto signUpDto) {
        User user = userService.signUpUser(signUpDto);
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JWTDto(user, token));
    }

    @PostMapping(path = "auth/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        User user = userService.loginUser(loginDto);
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JWTDto(user, token));
    }

    @PutMapping("user/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserDto dto
    ) {
        User updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/user/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable UUID id,
                                            @RequestBody PasswordChangeDto dto) {
        userService.changePassword(id, dto);
        return ResponseEntity.ok().build();
    }

}
