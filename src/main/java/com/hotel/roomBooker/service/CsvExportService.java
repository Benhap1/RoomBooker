package com.hotel.roomBooker.service;

import com.hotel.roomBooker.model.RoomBookingEvent;
import com.hotel.roomBooker.model.UserRegistrationEvent;
import com.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CsvExportService {

    private final MongoTemplate mongoTemplate;

    public InputStreamResource exportStatisticsToCsvAsResource() throws IOException {
        List<UserRegistrationEvent> userRegistrations = mongoTemplate.findAll(UserRegistrationEvent.class, "userRegistrations");
        List<RoomBookingEvent> roomBookings = mongoTemplate.findAll(RoomBookingEvent.class, "roomBookings");
        File file = File.createTempFile("statistics", ".csv");
        file.deleteOnExit();

        try (FileWriter outputfile = new FileWriter(file);
             CSVWriter writer = new CSVWriter(outputfile)) {

            String[] header = {"EventType", "UserID", "UserName", "CheckInDate", "CheckOutDate"};
            writer.writeNext(header);

            for (UserRegistrationEvent event : userRegistrations) {
                String[] data = {
                        "UserRegistration",
                        event.getUserId().toString(),
                        event.getUserName(),
                        "",
                        ""
                };
                writer.writeNext(data);
            }

            for (RoomBookingEvent event : roomBookings) {
                String[] data = {
                        "RoomBooking",
                        event.getUserId().toString(),
                        "",
                        event.getCheckInDate().toString(),
                        event.getCheckOutDate().toString()
                };
                writer.writeNext(data);
            }
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return resource;
    }
}
