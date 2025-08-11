package com.antonk.gymtracker.service;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {

    User signUpUser(@RequestBody SignUpDto signUpDto);

    User loginUser(@RequestBody LoginDto loginDto);
}
