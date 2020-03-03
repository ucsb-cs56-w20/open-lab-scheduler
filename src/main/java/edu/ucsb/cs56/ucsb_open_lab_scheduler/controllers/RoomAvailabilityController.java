package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlot;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomAvailabilityController {
    private static Logger log = LoggerFactory.getLogger(RoomAvailabilityController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    CSVToObjectService<RoomAvailability> csvToObjectService;


    @Autowired
    RoomAvailabilityRepository roomAvailabilityRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @GetMapping("/roomAvailability")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("RoomAvailabilityModel", roomAvailabilityRepository.findAll());
        return "roomAvailability/roomAvailability";
    }

    @PostMapping("/roomAvailability/upload")
    public String uploadCSV(@RequestParam("csv") MultipartFile csv, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        try(Reader reader = new InputStreamReader(csv.getInputStream())){
            List<RoomAvailability> roomAvails = csvToObjectService.parse(reader, RoomAvailability.class);
            roomAvailabilityRepository.saveAll(roomAvails);
            List<TimeSlot> timeSlots = new ArrayList<>();
            for ( RoomAvailability ra: roomAvails){
                int start = ra.getStartTime();
                int end = ra.getEndTime();
                while ( start <= end-30){
                    TimeSlot ts = new TimeSlot();
                    ts.setRoomAvailability(ra);
                    ts.setStartTime(start);
                    if (start%100 >= 30){
                        start += 70;
                        ts.setEndTime(start);
                    } else {
                        start+=30;
                        ts.setEndTime(start);
                    }
                    timeSlots.add(ts);
                }
            }
            timeSlotRepository.saveAll(timeSlots);
        }catch(IOException e){
            log.error(e.toString());
        }
        return "redirect:/roomAvailability";
    }
}
