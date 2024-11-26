package com.example.device_microservice.publisher;

import com.example.device_microservice.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevicePublisher {

    @Autowired
    private PostRequestSender postRequestSender;

    @Autowired
    private PutRequestSender putRequestSender;

    @Autowired
    private DeleteRequestSender deleteRequestSender;

    public void sendPostRequest(DeviceDTO dto) {
        postRequestSender.sendMessage(dto.getDeviceId(), dto.getUserId(), dto.getMaxHourlyConsumption());
    }

    public void sendPutRequest(DeviceDTO dto) {
        putRequestSender.sendMessage(dto.getDeviceId(), dto.getUserId(), dto.getMaxHourlyConsumption());
    }

    public void sendDeleteRequest(Long deviceId) {
        deleteRequestSender.sendMessage(deviceId, null, null);
    }
}