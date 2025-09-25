package com.antonk.gymtracker.service.impl;

import com.antonk.gymtracker.dto.*;
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

import java.util.Objects;
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

    public UserFetchDto getUserByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return new UserFetchDto(user.getUserId(), user.getUserFirstName(), user.getUserLastName(), user.getUserLoginId(), user.getUnitPreference());
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

    @Transactional
    @Override
    public User updateUser(UUID userId, UpdateUserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        boolean noChanges = Objects.equals(user.getUserFirstName(), dto.userFirstName()) &&
                Objects.equals(user.getUserLastName(), dto.userLastName()) &&
                Objects.equals(user.getUserLoginId(), dto.userLoginId()) &&
                Objects.equals(user.getUnitPreference(), dto.unitPreference());

        user.setUserFirstName(dto.userFirstName());
        user.setUserLastName(dto.userLastName());
        user.setUserLoginId(dto.userLoginId());

        if (dto.unitPreference() != null) {
            user.setUnitPreference(dto.unitPreference());
        }

        if (noChanges) {
            throw new AppException("No changes were made", HttpStatus.NOT_MODIFIED);
        }

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void changePassword(UUID userId, PasswordChangeDto passwordChangeDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));


        // check if old password matches
        if (bCryptPasswordEncoder.matches(passwordChangeDto.oldPassword(), user.getPassword())) {
            // encode and save new password
            user.setPassword(bCryptPasswordEncoder.encode(passwordChangeDto.newPassword()));
            userRepository.save(user);

        } else{
            throw new AppException("Old password is incorrect",  HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteUser(UUID userId){
        userRepository.deleteById(userId);
    }
}
