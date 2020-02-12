package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;

@Service
public class CSVToRoomAvailabilityServiceImpl implements CSVToRoomAvailabilityService {
    private static Logger log = LoggerFactory.getLogger(CSVToRoomAvailabilityServiceImpl.class);

    @Override
    public List<RoomAvailability> parse(Reader csv) {
        return new CsvToBeanBuilder<RoomAvailability>(csv).withSkipLines(1).withType(RoomAvailability.class).build().parse();
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
