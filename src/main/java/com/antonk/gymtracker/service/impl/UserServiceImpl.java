package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.repository.UserRepository;
import com.antonk.gymtracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;

@Service
@AllArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User signUpUser(SignUpDto signUpDto) {
        User user = new User(
                signUpDto.userLoginId(),
                signUpDto.userFirstName(),
                signUpDto.userLastName(),
                signUpDto.userPassword()
        );
        user.setUserPassword(bCryptPasswordEncoder.encode(CharBuffer.wrap(signUpDto.userPassword())));
        userRepository.save(user);
        return user;
    }

    public User loginUser(LoginDto loginDto) {
        User user = userRepository.findByUserLoginId(loginDto.userLoginId());

        if (bCryptPasswordEncoder.matches(loginDto.userPassword(), user.getUserPassword())) {
            return user;
        }
        throw new UsernameNotFoundException("Invalid username or password");
    }
}
