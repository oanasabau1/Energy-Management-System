package com.example.device_microservice.controller;

import com.example.device_microservice.authorization.JwtTokenValidator;
import com.example.device_microservice.dto.DeviceDTO;
import com.example.device_microservice.dto.DeviceRegisterDTO;
import com.example.device_microservice.dto.UserDTO;
import com.example.device_microservice.publisher.DevicePublisher;
import com.example.device_microservice.service.DeviceService;
import com.example.device_microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private DevicePublisher devicePublisher;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @PostMapping("/device")
    public ResponseEntity<DeviceDTO> registerDevice(@RequestBody DeviceRegisterDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build(); // Return 401 Unauthorized if token is invalid
        }

        String username = jwtTokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        DeviceDTO registeredDevice = deviceService.registerDevice(dto);
        devicePublisher.sendPostRequest(registeredDevice);
        return ResponseEntity.ok(registeredDevice);
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceDTO>> getAllDevices(@RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtTokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        List<DeviceDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/device/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") Long deviceId,
                                               @RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        DeviceDTO device = deviceService.getDeviceById(deviceId);
        return ResponseEntity.ok(device);
    }

    @GetMapping("/user/{id}/devices")
    public ResponseEntity<List<DeviceDTO>> getAllDevicesByUserId(@PathVariable("id") Long userId,
                                                                 @RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }
        List<DeviceDTO> devices = deviceService.getAllDevicesByUserId(userId);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/device/update/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id") Long deviceId,
                                                  @RequestBody DeviceDTO dto,
                                                  @RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtTokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        UserDTO user = userService.getUserById(dto.getUserId());
        DeviceDTO updatedDevice = deviceService.updateDevice(deviceId, dto);
        devicePublisher.sendPutRequest(updatedDevice);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/device/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Long deviceId,
                                          @RequestHeader("Authorization") String token) {
        if (!jwtTokenValidator.validateJwtToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtTokenValidator.extractUsernameFromToken(token);
        if (!username.equals("admin")) {
            return ResponseEntity.status(403).build(); // Forbidden if not admin
        }

        deviceService.deleteDevice(deviceId);
        devicePublisher.sendDeleteRequest(deviceId);
        return ResponseEntity.ok().build();
    }
}
