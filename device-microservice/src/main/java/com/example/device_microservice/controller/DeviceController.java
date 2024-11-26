package com.example.device_microservice.controller;

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

    @PostMapping("/device")
    ResponseEntity<DeviceDTO> registerDevice(@RequestBody DeviceRegisterDTO dto){
        DeviceDTO registeredDevice = deviceService.registerDevice(dto);
        devicePublisher.sendPostRequest(registeredDevice);
        return ResponseEntity.ok(registeredDevice);
    }

    @GetMapping("/devices")
    ResponseEntity<List<DeviceDTO>> getAllDevices(){
        List<DeviceDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/device/{id}")
    ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") Long deviceId){
        DeviceDTO device = deviceService.getDeviceById(deviceId);
        return ResponseEntity.ok(device);
    }

    @GetMapping("/user/{id}/devices")
    ResponseEntity<List<DeviceDTO>> getAllDevicesByUserId(@PathVariable("id") Long userId){
        List<DeviceDTO> devices = deviceService.getAllDevicesByUserId(userId);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/device/update/{id}")
    ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id") Long deviceId, @RequestBody DeviceDTO dto){
        UserDTO user = userService.getUserById(dto.getUserId());
        DeviceDTO updatedDevice = deviceService.updateDevice(deviceId, dto);
        devicePublisher.sendPutRequest(updatedDevice);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/device/delete/{id}")
    ResponseEntity<DeviceDTO> deleteDevice(@PathVariable("id") Long deviceId){
        deviceService.deleteDevice(deviceId);
        devicePublisher.sendDeleteRequest(deviceId);
        return ResponseEntity.ok().build();
    }
}