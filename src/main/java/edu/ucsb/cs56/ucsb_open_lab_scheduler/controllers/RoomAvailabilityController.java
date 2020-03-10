package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlot;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.RoomAvailabilityDownloadCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import com.opencsv.CSVWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import javax.validation.Valid;

@Controller
public class RoomAvailabilityController {
    private static Logger logger = LoggerFactory.getLogger(RoomAvailabilityController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    CSVToObjectService<RoomAvailability> csvToObjectService;

    @Autowired
    RoomAvailabilityRepository roomAvailabilityRepository;

    @Autowired
    RoomRepository roomRepository;

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
        model.addAttribute("roomAvailabilityModel", roomAvailabilityRepository.findAll());
        return "roomAvailability/roomAvailability";
    }


    @GetMapping("/roomAvailability/export-CSV")
    public String exportCSV(HttpServletResponse response, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) throws IOException{
          String role = authControllerAdvice.getRole(token);
          if (!(role.equals("Admin"))) {
              redirAttrs.addFlashAttribute("alertDanger", "You do not have per    mission to access that page");
              return "redirect:/";
          }
          String filename = "roomAvailability.csv";
          response.setContentType("text/csv");
          response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                  "attachment; filename=\"" + filename + "\"");
          List<RoomAvailability> rooms = (List<RoomAvailability>)roomAvailabilityRepository.findAll();
          RoomAvailabilityDownloadCSV.writeRoomAvailability(response.getWriter(),rooms);
          return "redirect:/roomAvailability";
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
            logger.error(e.toString());
        } catch (RuntimeException a){
            redirAttrs.addFlashAttribute("alertDanger", "Please enter a correct csv file.");
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
        model.addAttribute("ra", new RoomAvailability());
        model.addAttribute("room", new Room());
        return "roomAvailability/edit";

    }

    @PostMapping("/roomAvailability/create")
    public String create(@ModelAttribute @Valid RoomAvailability ra, BindingResult result, OAuth2AuthenticationToken token, 
        RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorMessage = "";
            for(int i = 0; i < errors.size(); i++) {
                errorMessage += errors.get(i).getDefaultMessage();
                if(i != errors.size() - 1) {
                    errorMessage += ", ";
                }
            }
            redirAttrs.addFlashAttribute("alertDanger", errorMessage);
            return "redirect:/roomAvailability";
        }
        if (!role.equals("Admin")) {
            return "index";
        }
        Room r = roomRepository.findByName(ra.getRoom().getName());
        if(r == null) {
            roomRepository.save(new Room(ra.getRoom().getName()));
        } 
        roomAvailabilityRepository.save(ra);

        return "redirect:/roomAvailability";
    }

    @GetMapping("/roomAvailability/edit/{id}")
    public String editEntry(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token,
            RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "index";
        }

        model.addAttribute("ra", roomAvailabilityRepository.findById(id));
        model.addAttribute("raExists", true);
        model.addAttribute("raID", id);
        model.addAttribute("defaultDuration", defaultDuration);
        return "roomAvailability/edit";
    }

    @PostMapping("/roomAvailability/save")
    public String save(@ModelAttribute @Valid RoomAvailability ra, BindingResult result, @RequestParam("id") long id,
        OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return "index";
        }
        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorMessage = "";
            for(int i = 0; i < errors.size(); i++) {
                errorMessage += errors.get(i).getDefaultMessage();
                if(i != errors.size() - 1) {
                    errorMessage += ", ";
                }
            }
            redirAttrs.addFlashAttribute("alertDanger", errorMessage);
            return "redirect:/roomAvailability";
        }
        RoomAvailability existingRA = roomAvailabilityRepository.findById(id);
        existingRA.setQuarter(ra.getQuarter());
        existingRA.setDay(ra.getDay());
        existingRA.setStartTime(ra.getStartTime());
        existingRA.setEndTime(ra.getEndTime());
        Room r = roomRepository.findByName(ra.getRoom().getName());
        if(r == null) {
            roomRepository.save(new Room(ra.getRoom().getName()));
        } else {
            existingRA.setRoom(r);
        }
        roomAvailabilityRepository.save(ra);

        return "redirect:/roomAvailability";
    }

    @PostMapping("/roomAvailability/delete/{id}")
    public String deleteEntry(@PathVariable("id") long id, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return "index";
        }
        
        List<TimeSlot> timeSlots= timeSlotRepository.findByRoomAvailabilityId(id);
        if(!timeSlots.isEmpty()){
            for(TimeSlot ts: timeSlots){
                timeSlotRepository.deleteById(ts.getId());
            }
        }
        roomAvailabilityRepository.deleteById(id);

        return "redirect:/roomAvailability";
    }
}
