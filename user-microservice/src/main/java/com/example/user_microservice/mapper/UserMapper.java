package com.example.user_microservice.mapper;

import com.example.user_microservice.dto.UserDTO;
import com.example.user_microservice.dto.UserRegisterDTO;
import com.example.user_microservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {
    public User toUser(UserRegisterDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setIsAdmin(dto.getIsAdmin());
        return user;
    }

    public UserDTO toDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setIsAdmin(user.getIsAdmin());
        return dto;
    }
}