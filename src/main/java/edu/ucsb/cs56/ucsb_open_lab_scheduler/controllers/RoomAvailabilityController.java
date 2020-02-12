package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToRoomAvailabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
public class RoomAvailabilityController {
    private static Logger log = LoggerFactory.getLogger(RoomAvailabilityController.class);

    @Autowired
    CSVToRoomAvailabilityService csvtra;

    @Autowired
    RoomAvailabilityRepository rar;

    @GetMapping("/roomAvailability")
    public String dashboard() {
        return "roomAvailability";
    }

    @PostMapping("/roomAvailability/upload")
    public String uploadCSV(@RequestParam("csv") MultipartFile csv) {
        try {
            Reader reader = new InputStreamReader(csv.getInputStream());
            List<RoomAvailability> roomAvails = csvtra.parse(reader);
            rar.saveAll(roomAvails);
        }catch(IOException e){
            log.info(e.toString());
        }

        return "redirect:/roomAvailability";
    }
}
