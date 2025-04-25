package com.mtran.mvc.service.impl;

import com.mtran.mvc.dto.UserDTO;
import com.mtran.mvc.entity.User;
import com.mtran.mvc.mapper.UserMapper;
import com.mtran.mvc.repository.UserRepository;
import com.mtran.mvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO findByEmail(String email) {
        return userMapper.toUserDTO(userRepository.findByEmail(email));
    }

    @Override
    public void createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("user name existed with : " + userDTO.getEmail());
        }
        User user = userMapper.toUserEntity(userDTO);
        //BCRYPT
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            throw new RuntimeException("user name not existed with : " + userDTO.getEmail());
        }
        user.setName(userDTO.getName());
        user.setDob(userDTO.getDob());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email, String password) {
        User user= userRepository.findByEmail(email);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user= userRepository.findByEmail(email);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is not correct");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
