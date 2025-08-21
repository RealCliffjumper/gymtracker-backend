package com.antonk.gymtracker.controller;

import com.antonk.gymtracker.JWT.JWTTokenProvider;
import com.antonk.gymtracker.dto.*;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PutMapping("user/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable UUID userId,
            @RequestBody UpdateUserDto dto
    ) {
        User updated = userService.updateUser(userId, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/user/{userId}/password")
    public ResponseEntity<?> changePassword(@PathVariable UUID userId,
                                            @RequestBody PasswordChangeDto dto) {
        userService.changePassword(userId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "user/delete/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") UUID userId){
        userService.deleteUser(userId);
    }
}
