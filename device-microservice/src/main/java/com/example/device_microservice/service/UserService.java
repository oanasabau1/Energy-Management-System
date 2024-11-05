package com.example.device_microservice.service;

import com.example.device_microservice.dto.UserDTO;
import com.example.device_microservice.entity.User;
import com.example.device_microservice.mapper.UserMapper;
import com.example.device_microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return userMapper.toDTO(user.get());
        } else {
            throw new InvalidParameterException("There is no user with userId " + userId);
        }
    }

    public UserDTO registerUser(UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new InvalidParameterException("Username already exists");
        }
        User savedUser = userRepository.save(userMapper.toUser(dto));
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(Long userId, UserDTO dto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User savedUser = user.get();
            savedUser.setUsername(dto.getUsername());
            userRepository.save(savedUser);
            return userMapper.toDTO(savedUser);
        } else {
            throw new InvalidParameterException("There is no user with userId " + userId);
        }
    }

    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new InvalidParameterException("There is no user with userId " + userId);
        }
    }
}