package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
        } catch (IOException e) {
            log.error(e.toString());
        }
        return "redirect:/roomAvailability";
    }

    @GetMapping("/roomAvailability/create")
    public String createEntry(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        return "roomAvailability/create";

    }

    @PostMapping("/roomAvailability/add")
    public ResponseEntity<?> add(@RequestParam("quarter") String quarter, @RequestParam("day") String day, @RequestParam("start") String start,
            @RequestParam("end") String end, @RequestParam("room") String room, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        RoomAvailability roomAvailability = new RoomAvailability(quarter, Integer.parseInt(start), Integer.parseInt(end), day, room);
        
        roomAvailabilityRepository.save(roomAvailability);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roomAvailability/edit/{id}")
    public String editEntry(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("ra", roomAvailabilityRepository.findById(id).get());
        return "roomAvailability/edit";
    }

    @PostMapping("/roomAvailability/edit/save")
    public ResponseEntity<?> editSave(@RequestParam("id") String id, @RequestParam("quarter") String quarter, @RequestParam("day") String day, @RequestParam("start") String start,
            @RequestParam("end") String end, @RequestParam("room") String room, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        RoomAvailability ra = roomAvailabilityRepository.findById(Long.parseLong(id)).get();
        ra.setQuarter(quarter);
        ra.setDay(day);
        ra.setStartTime(Integer.parseInt(start));
        ra.setEndTime(Integer.parseInt(end));
        ra.setRoom(room);
        roomAvailabilityRepository.save(ra);
        
        
        

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
