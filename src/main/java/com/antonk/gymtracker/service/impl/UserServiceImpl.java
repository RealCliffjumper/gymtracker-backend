package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.LoginDto;
import com.antonk.gymtracker.dto.PasswordChangeDto;
import com.antonk.gymtracker.dto.SignUpDto;
import com.antonk.gymtracker.dto.UpdateUserDto;
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
import java.util.UUID;

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
                signUpDto.createdAt(),
                signUpDto.unitPreference()
        );
        if(userRepository.findByUserLoginId(signUpDto.userLoginId()).isPresent()) {
            throw new AppException("Username already exists",  HttpStatus.CONFLICT);
        }
        user.setPassword(bCryptPasswordEncoder.encode(signUpDto.password()));
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

    @Override
    public User updateUser(UUID id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserFirstName(dto.userFirstName());
        user.setUserLastName(dto.userLastName());
        user.setUserLoginId(dto.userLoginId());

        if (dto.unitPreference() != null) {
            user.setUnitPreference(dto.unitPreference());
        }

        return userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, PasswordChangeDto passwordChangeDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // check if old password matches
        if (bCryptPasswordEncoder.matches(passwordChangeDto.oldPassword(), user.getPassword())) {
            // encode and save new password
            user.setPassword(bCryptPasswordEncoder.encode(passwordChangeDto.newPassword()));
            userRepository.save(user);

        } else{
            throw new RuntimeException("Old password is incorrect" + " " + passwordChangeDto.oldPassword() + " " + user.getPassword());
        }

    }
}
