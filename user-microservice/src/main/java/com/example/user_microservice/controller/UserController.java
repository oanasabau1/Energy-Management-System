package com.example.user_microservice.controller;

import com.example.user_microservice.authorization.JwtTokenValidator;
import com.example.user_microservice.dto.UserDTO;
import com.example.user_microservice.dto.UserRegisterDTO;
import com.example.user_microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenValidator tokenValidator;

    @PostMapping("/user")
    ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO dto,
                                       @RequestHeader("Authorization") String token) {
        if (!tokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build(); // Return 401 Unauthorized if token is invalid
        }

        String username = tokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        UserDTO registeredUser = userService.registerUser(dto);

        WebClient client = WebClient.create("http://device-microservice-spring:8081");
        try {
            ResponseEntity<?> responseEntity = client.post()
                    .uri("/user")
                    .bodyValue(registeredUser)
                    .retrieve()
                    .toEntity(Void.class)
                    .block();
            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException("Error while registering user to device management service");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while registering user to device management service", e);
        }
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
        if (!tokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = tokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/user/update/{id}")
    ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userId, @RequestBody UserDTO dto,
                                       @RequestHeader("Authorization") String token) {
        if (!tokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = tokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        UserDTO updatedUser = userService.updateUser(userId, dto);

        WebClient client = WebClient.create("http://device-microservice-spring:8081");
        try {
            ResponseEntity<?> responseEntity = client.put()
                    .uri("/user/update/{id}", userId)
                    .bodyValue(updatedUser)
                    .retrieve()
                    .toEntity(Void.class)
                    .block();
            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException("Error while updating user in device management service");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating user in device management service", e);
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId,
                                 @RequestHeader("Authorization") String token) {
        if (!tokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = tokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        userService.deleteUser(userId);

        WebClient client = WebClient.create("http://device-microservice-spring:8081");
        try {
            ResponseEntity<?> responseEntity = client.delete()
                    .uri("/user/delete/{id}", userId)
                    .retrieve()
                    .toEntity(Void.class)
                    .block();
            if (responseEntity.getStatusCode().isError()) {
                throw new RuntimeException("Error while deleting user from device management service");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user from device management service", e);
        }
        return ResponseEntity.ok().build();
    }
}