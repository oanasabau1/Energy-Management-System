package com.example.device_microservice.service;

import com.example.device_microservice.dto.DeviceDTO;
import com.example.device_microservice.dto.DeviceRegisterDTO;
import com.example.device_microservice.entity.Device;
import com.example.device_microservice.mapper.DeviceMapper;
import com.example.device_microservice.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
     private DeviceRepository deviceRepository;
    @Autowired
    private DeviceMapper deviceMapper;

    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(deviceMapper::toDTO).toList();
    }

    public List<DeviceDTO> getAllDevicesByUserId(Long userId) {
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        return devices.stream().map(deviceMapper::toDTO).toList();
    }

        public DeviceDTO registerDevice(DeviceRegisterDTO dto) {
        Device savedDevice = deviceRepository.save(deviceMapper.toDevice(dto));
        return deviceMapper.toDTO(savedDevice);
    }

    public DeviceDTO updateDevice(Long deviceId, DeviceDTO dto) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if(device.isPresent()){
            Device savedDevice = device.get();
            savedDevice.setDescription(dto.getDescription());
            savedDevice.setAddress(dto.getAddress());
            savedDevice.setMaxHourlyConsumption(dto.getMaxHourlyConsumption());
            return deviceMapper.toDTO(deviceRepository.save(savedDevice));
        }
        else{
            throw new InvalidParameterException("There is no device with deviceId " + dto.getDeviceId());
        }
    }

    public void deleteDevice(Long deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if(device.isPresent()){
            deviceRepository.delete(device.get());
        }
        else{
            throw new InvalidParameterException("There is no device with deviceId " + deviceId);
        }
    }

    public void deleteAllDevicesByUserId(Long userId) {
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        if(!devices.isEmpty()){
            deviceRepository.deleteAll(devices);
        }
    }
}