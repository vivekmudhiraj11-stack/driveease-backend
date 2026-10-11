package com.driveease.service;

import com.driveease.model.Booking;
import com.driveease.model.Vehicle;
import com.driveease.repository.BookingRepository;
import com.driveease.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;

    public BookingService(BookingRepository bookingRepository, VehicleRepository vehicleRepository) {
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Booking createBooking(Booking booking) {
        if (booking == null || booking.getVehicleId() == null || booking.getVehicleId().isBlank())
            throw new IllegalArgumentException("Vehicle is required");
        if (booking.getStartDate() == null || booking.getEndDate() == null)
            throw new IllegalArgumentException("Start date and end date are required");
        if (booking.getStartDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Start date cannot be in the past");
        if (!booking.getEndDate().isAfter(booking.getStartDate()))
            throw new IllegalArgumentException("End date must be after start date");

        Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId())
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        if (!vehicle.isAvailable()) throw new IllegalArgumentException("Vehicle is not available");

        List<Booking> overlaps = bookingRepository
            .findByVehicleIdAndStatusInAndStartDateLessThanAndEndDateGreaterThan(
                booking.getVehicleId(), List.of("PENDING", "CONFIRMED"),
                booking.getEndDate(), booking.getStartDate());
        if (!overlaps.isEmpty())
            throw new IllegalArgumentException("Vehicle is already booked for the selected dates");

        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        booking.setTotalPrice(days * vehicle.getPricePerDay());
        booking.setStatus("PENDING");
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() { return bookingRepository.findAll(); }

    public List<Booking> getBookingsByUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public Booking cancelBooking(String id, String userEmail) {
        Booking booking = getBookingById(id);
        if (!booking.getUserEmail().equalsIgnoreCase(userEmail))
            throw new IllegalArgumentException("You can only cancel your own bookings");
        if ("CANCELLED".equals(booking.getStatus()))
            throw new IllegalArgumentException("Booking is already cancelled");
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}
