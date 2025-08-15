package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.entity.User;
import com.antonk.gymtracker.exception.AppException;
import com.antonk.gymtracker.repository.UserRepository;
import com.antonk.gymtracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;

@Service
@AllArgsConstructor
class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {
        return this.userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Transactional
    public User signUpUser(SignUpDto signUpDto) {
        User user = new User(
                signUpDto.userFirstName(),
                signUpDto.userLastName(),
                signUpDto.userLoginId(),
                signUpDto.password(),
                signUpDto.createdAt()
        );
        if(userRepository.findByUserLoginId(signUpDto.userLoginId()).isPresent()) {
            throw new AppException("Username already exists",  HttpStatus.CONFLICT);
        }
        user.setPassword(bCryptPasswordEncoder.encode(CharBuffer.wrap(signUpDto.password())));
        userRepository.save(user);
        return user;
    }

    public User loginUser(LoginDto loginDto) {
        User user = userRepository.findByUserLoginId(loginDto.userLoginId())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (bCryptPasswordEncoder.matches(loginDto.password(), user.getPassword())) {
            return user;
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }
}
