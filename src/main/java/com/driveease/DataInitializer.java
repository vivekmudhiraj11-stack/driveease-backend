package com.driveease;

import com.driveease.model.User;
import com.driveease.model.Vehicle;
import com.driveease.repository.UserRepository;
import com.driveease.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedDemoData(VehicleRepository vehicles, UserRepository users,
                                   PasswordEncoder encoder,
                                   @Value("${DRIVEEASE_ADMIN_EMAIL:}") String adminEmail,
                                   @Value("${DRIVEEASE_ADMIN_PASSWORD:}") String adminPassword) {
        return args -> {
            if (vehicles.count() == 0) {
                vehicles.save(vehicle("Maruti Suzuki", "Swift", "Hatchback", "TS 09 AB 1001", 1800,
                    "Petrol", 5, "Manual", "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=1200&q=80"));
                vehicles.save(vehicle("Hyundai", "Creta", "SUV", "TS 09 AB 1002", 3200,
                    "Petrol", 5, "Automatic", "https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?auto=format&fit=crop&w=1200&q=80"));
                vehicles.save(vehicle("Honda", "City", "Sedan", "TS 09 AB 1003", 2600,
                    "Petrol", 5, "Automatic", "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=1200&q=80"));
                vehicles.save(vehicle("Toyota", "Innova Crysta", "MUV", "TS 09 AB 1004", 4200,
                    "Diesel", 7, "Manual", "https://images.unsplash.com/photo-1511919884226-fd3cad34687c?auto=format&fit=crop&w=1200&q=80"));
                vehicles.save(vehicle("Tata", "Nexon EV", "Electric", "TS 09 AB 1005", 3000,
                    "Electric", 5, "Automatic", "https://images.unsplash.com/photo-1593941707882-a5bba14938c7?auto=format&fit=crop&w=1200&q=80"));
                vehicles.save(vehicle("Mahindra", "Thar", "SUV", "TS 09 AB 1006", 4500,
                    "Diesel", 4, "Manual", "https://images.unsplash.com/photo-1519608487953-e999c86e7455?auto=format&fit=crop&w=1200&q=80"));
            }
            // Optional admin account: configure both DRIVEEASE_ADMIN_EMAIL and DRIVEEASE_ADMIN_PASSWORD.
            if (adminEmail != null && !adminEmail.isBlank() && adminPassword != null
                    && adminPassword.length() >= 8 && users.findByEmail(adminEmail.trim().toLowerCase()).isEmpty()) {
                User admin = new User();
                admin.setName("DriveEase Administrator");
                admin.setEmail(adminEmail.trim().toLowerCase());
                admin.setPassword(encoder.encode(adminPassword));
                admin.setRole("ADMIN");
                users.save(admin);
            }
        };
    }

    private Vehicle vehicle(String brand, String model, String type, String reg, double price,
                            String fuel, int seats, String transmission, String image) {
        Vehicle v = new Vehicle();
        v.setBrand(brand); v.setModel(model); v.setType(type); v.setRegistrationNumber(reg);
        v.setPricePerDay(price); v.setFuelType(fuel); v.setSeats(seats);
        v.setTransmission(transmission); v.setImageUrl(image); v.setAvailable(true);
        return v;
    }
}
