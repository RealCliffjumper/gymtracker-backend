package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.*;
import com.antonk.gymtracker.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface UserService {

    UserFetchDto getUserByUserId(UUID userId);

    User signUpUser(@RequestBody SignUpDto signUpDto);

    User loginUser(@RequestBody LoginDto loginDto);

    User updateUser(UUID userId, UpdateUserDto userDto);

    void changePassword(UUID userId, @RequestBody PasswordChangeDto passwordChangeDto);

    void changeThemePreference(UUID userId, boolean isThemeDark);

    void deleteUser(UUID userId);
}
