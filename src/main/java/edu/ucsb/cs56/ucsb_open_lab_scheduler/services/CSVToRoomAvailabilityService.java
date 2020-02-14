package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public interface CSVToRoomAvailabilityService {
    List<RoomAvailability> parse(Reader csv);

    Logger getLogger();

    default List<RoomAvailability> parse(MultipartFile file) {
        try {
            return parse(new InputStreamReader(file.getInputStream()));
        } catch (IOException e) {
            getLogger().error("CSV could not be parsed", e);
        }
        return null;
    }
}
