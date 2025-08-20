package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.PasswordChangeDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.dto.UpdateUserDto;
import com.antonk.gymtracker.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface UserService {

    User signUpUser(@RequestBody SignUpDto signUpDto);

    User loginUser(@RequestBody LoginDto loginDto);

    User updateUser(UUID id, UpdateUserDto userDto);

    void changePassword(UUID id, @RequestBody PasswordChangeDto passwordChangeDto);
}
