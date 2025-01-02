package com.example.user_microservice.controller;

import com.example.user_microservice.authorization.JwtTokenGenerator;
import com.example.user_microservice.dto.UserDTO;
import com.example.user_microservice.dto.UserLoginDTO;
import com.example.user_microservice.security.Security;
import com.example.user_microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        try {
            UserDTO user = userService.getUserByUsername(dto.getUsername());
            if (user != null && user.getPassword().equals(Security.encryptPassword(dto.getPassword()))) {
                String token = jwtTokenGenerator.generateJwtToken(user.getUsername());
                Map<String, Object> response = new HashMap<>();
                response.put("user", user);
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Wrong password or user not found");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while processing the request.");
        }
    }
}
