package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlot;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


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
    @Value("${app.timeSlotDefaultDuration}")
    private int defaultDuration;

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
    public String uploadCSV(@RequestParam("csv") MultipartFile csv, OAuth2AuthenticationToken token,
            RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        try (Reader reader = new InputStreamReader(csv.getInputStream())) {
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
        } catch (IOException e) {
            log.error(e.toString());
        }catch(RuntimeException a){
            redirAttrs.addFlashAttribute("alertDanger", "Please enter the correct csv files.");
            return "redirect:/roomAvailability";
        }
        return "redirect:/roomAvailability";
    }

    @GetMapping("/roomAvailability/new")
    public String newEntry(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("raExists", false);
        model.addAttribute("defaultDuration", defaultDuration);
        return "roomAvailability/edit";

    }

    @PostMapping("/roomAvailability/create")
    public ResponseEntity<?> create(@RequestParam("quarter") String quarter, @RequestParam("day") String day,
            @RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("room") String room,
            OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        RoomAvailability roomAvailability = new RoomAvailability(quarter, Integer.parseInt(start),
                Integer.parseInt(end), day, new Room(room));

        roomAvailabilityRepository.save(roomAvailability);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roomAvailability/{id}/edit")
    public String editEntry(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token,
            RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        model.addAttribute("ra", roomAvailabilityRepository.findById(id));
        model.addAttribute("raDay", roomAvailabilityRepository.findById(id).getDay());
        model.addAttribute("raExists", true);
        model.addAttribute("raID", id);
        model.addAttribute("defaultDuration", defaultDuration);
        return "roomAvailability/edit";
    }

    @PutMapping("/roomAvailability/save")
    public ResponseEntity<?> save(@RequestParam("id") String id, @RequestParam("quarter") String quarter,
            @RequestParam("day") String day, @RequestParam("start") String start, @RequestParam("end") String end,
            @RequestParam("room") String room, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        RoomAvailability ra = roomAvailabilityRepository.findById(Long.parseLong(id));
        ra.setQuarter(quarter);
        ra.setDay(day);
        ra.setStartTime(Integer.parseInt(start));
        ra.setEndTime(Integer.parseInt(end));
        ra.setRoom(new Room(room));
        roomAvailabilityRepository.save(ra);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/roomAvailability/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable("id") long id, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        roomAvailabilityRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
