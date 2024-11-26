package com.example.user_microservice.controller;

import com.example.user_microservice.dto.UserDTO;
import com.example.user_microservice.dto.UserLoginDTO;
import com.example.user_microservice.dto.UserRegisterDTO;
import com.example.user_microservice.service.UserService;
import com.example.user_microservice.util.Security;
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

    @PostMapping ("/login")
    ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        try {
            UserDTO user = userService.getUserByUsername(dto.getUsername());
            if (user.getPassword().equals(Security.encryptPassword(dto.getPassword()))) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.badRequest().body("Wrong password");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/user")
    ResponseEntity<UserDTO> createUser(@RequestBody UserRegisterDTO dto){
        UserDTO registeredUser = userService.registerUser(dto);

        WebClient client = WebClient.create("http://device-microservice-spring:8081");
        ResponseEntity<?> responseEntity = client.post()
                .uri("/user")
                .bodyValue(registeredUser)
                .retrieve()
                .toEntity(Void.class)
                .block();
        if(responseEntity.getStatusCode().isError()){
            throw new RuntimeException("Error while registering user to device management service");
        }
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/user/update/{id}")
    ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userId, @RequestBody UserDTO dto){
        UserDTO updatedUser = userService.updateUser(userId, dto);

        WebClient client = WebClient.create("http://device-microservice-spring:8081");
        ResponseEntity<?> responseEntity = client.put()
                .uri("/user/update/{id}", userId)
                .bodyValue(updatedUser)
                .retrieve()
                .toEntity(Void.class)
                .block();
        if(responseEntity.getStatusCode().isError()){
            throw new RuntimeException("Error while updating user in device management service");
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);

        WebClient client = WebClient.create("http://device-microservice-spring:8081");
        ResponseEntity<?> responseEntity = client.delete()
                .uri("/user/delete/{id}", userId)
                .retrieve()
                .toEntity(Void.class)
                .block();
        if(responseEntity.getStatusCode().isError()){
            throw new RuntimeException("Error while deleting user from device management service");
        }
        return ResponseEntity.ok().build();
    }
}