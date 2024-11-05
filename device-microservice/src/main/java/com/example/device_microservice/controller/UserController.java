package com.example.device_microservice.controller;

import com.example.device_microservice.dto.UserDTO;
import com.example.device_microservice.service.DeviceService;
import com.example.device_microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user")
    ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        UserDTO registeredUser = userService.registerUser(dto);
        return ResponseEntity.ok(registeredUser);
    }

    @PutMapping("/user/update/{id}")
    ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userId, @RequestBody UserDTO dto) {
        UserDTO updatedUser = userService.updateUser(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        deviceService.deleteAllDevicesByUserId(userId);
        return ResponseEntity.ok().build();
    }
}