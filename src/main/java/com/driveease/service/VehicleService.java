package com.driveease.service;

import com.driveease.model.Vehicle;
import com.driveease.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        validate(vehicle);
        vehicle.setRegistrationNumber(vehicle.getRegistrationNumber().trim().toUpperCase());
        if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber()))
            throw new IllegalArgumentException("Vehicle registration number already exists");
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
    }

    public Vehicle updateVehicle(String id, Vehicle updatedVehicle) {
        validate(updatedVehicle);
        Vehicle existing = getVehicleById(id);
        String registration = updatedVehicle.getRegistrationNumber().trim().toUpperCase();
        if (!registration.equalsIgnoreCase(existing.getRegistrationNumber())
                && vehicleRepository.existsByRegistrationNumber(registration))
            throw new IllegalArgumentException("Vehicle registration number already exists");
        existing.setBrand(updatedVehicle.getBrand().trim());
        existing.setModel(updatedVehicle.getModel().trim());
        existing.setType(updatedVehicle.getType());
        existing.setRegistrationNumber(registration);
        existing.setPricePerDay(updatedVehicle.getPricePerDay());
        existing.setFuelType(updatedVehicle.getFuelType());
        existing.setSeats(updatedVehicle.getSeats());
        existing.setTransmission(updatedVehicle.getTransmission());
        existing.setImageUrl(updatedVehicle.getImageUrl());
        existing.setAvailable(updatedVehicle.isAvailable());
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(String id) {
        vehicleRepository.delete(getVehicleById(id));
    }

    private void validate(Vehicle vehicle) {
        if (vehicle == null || blank(vehicle.getBrand()) || blank(vehicle.getModel())
                || blank(vehicle.getRegistrationNumber()))
            throw new IllegalArgumentException("Brand, model and registration number are required");
        if (vehicle.getPricePerDay() <= 0)
            throw new IllegalArgumentException("Price per day must be greater than zero");
        if (vehicle.getSeats() < 1 || vehicle.getSeats() > 20)
            throw new IllegalArgumentException("Seats must be between 1 and 20");
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
}
