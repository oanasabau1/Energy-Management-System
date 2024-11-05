package com.example.user_microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginDTO {
    String username;
    String password;
}
