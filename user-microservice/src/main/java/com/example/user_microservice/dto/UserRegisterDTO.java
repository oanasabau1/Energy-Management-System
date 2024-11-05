package com.example.user_microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterDTO {
    String username;
    String password;
    Boolean isAdmin;
}
