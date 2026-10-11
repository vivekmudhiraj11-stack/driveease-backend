package com.driveease.repository;

import com.driveease.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findByUserEmail(String userEmail);

    List<Booking> findByVehicleId(String vehicleId);

    List<Booking> findByVehicleIdAndStatusInAndStartDateLessThanAndEndDateGreaterThan(
            String vehicleId,
            List<String> statuses,
            LocalDate requestedEndDate,
            LocalDate requestedStartDate);
}