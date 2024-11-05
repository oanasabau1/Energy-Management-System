package com.example.user_microservice.service;

import com.example.user_microservice.dto.UserDTO;
import com.example.user_microservice.dto.UserRegisterDTO;
import com.example.user_microservice.entity.User;
import com.example.user_microservice.mapper.UserMapper;
import com.example.user_microservice.repository.UserRepository;
import com.example.user_microservice.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).collect(toList());
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return userMapper.toDTO(user);
        } else {
            throw new InvalidParameterException("There is no user with username " + username);
        }
    }

    public UserDTO registerUser(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new InvalidParameterException("Username already exists");
        }
        String hashedPassword = Security.encryptPassword(dto.getPassword());  // hash the password before saving to ensure security
        dto.setPassword(hashedPassword);
        User savedUser = userRepository.save(userMapper.toUser(dto));
        return userMapper.toDTO(savedUser);
    }

    public UserDTO updateUser(Long userId, UserDTO dto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User savedUser = user.get();
            savedUser.setUsername(dto.getUsername());
            savedUser.setPassword(Security.encryptPassword(dto.getPassword()));
            userRepository.save(savedUser);
            return userMapper.toDTO(savedUser);
        } else {
            throw new InvalidParameterException("There is no user with id " + userId);
        }
    }

    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new InvalidParameterException("There is no user with id " + userId);
        }
    }
}