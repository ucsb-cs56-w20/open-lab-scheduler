
package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorCheckIn;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorCheckInRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.ValidEmailService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.validation.Valid;

@Controller
public class TutorCheckInController {

    private Logger logger = LoggerFactory.getLogger(TutorCheckIn.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;
  
    @Autowired
    private TutorRepository tutorRepository;
    
    @Autowired
    private TutorCheckInRepository tutorcheckinRepository;
    @Autowired
    public TutorCheckInController(TutorCheckInRepository repo) {
        this.tutorcheckinRepository = repo;
    }

    @GetMapping("/tutorCheckIn")
    public String tutorcheckin(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {

        if (!authControllerAdvice.getIsTutor(token)) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        
        model.addAttribute("tutors", tutorcheckinRepository.findAll());
        model.addAttribute("newTutor", new TutorCheckIn());
        return "/tutorCheckIn/tutorCheckIn";
    }
    

    @GetMapping("/tutorCheckIn/viewLog")
    public String viewLog(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        //String role = authControllerAdvice.getRole(token);
        if (!authControllerAdvice.getIsTutor(token)) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        Optional<Tutor> tutor = tutorRepository.findByEmail(authControllerAdvice.getEmail(token));
        if (!tutor.isPresent()) {
            redirAttrs.addFlashAttribute("alertDanger", "Tutor with email " + authControllerAdvice.getEmail(token) + " not found");
            return "redirect:/";
        } 

        List<TutorCheckIn> tutorCheckIns = tutorcheckinRepository.findAll();
        model.addAttribute("tutorCheckIns", tutorCheckIns);
        logger.info("tutorCheckIns" + tutorCheckIns);
        //model.addAttribute("newTutor", new TutorCheckIn());
        return "/tutorCheckIn/viewLog";
    }

    @PostMapping("/tutorCheckIn/create")
    public ResponseEntity<?> create(@RequestParam("timeSlotAssignmentId") TimeSlotAssignment timeSlotAssignmentId, @RequestParam("time") String time,
            @RequestParam("date") String date, @RequestParam("remarks") String remarks,
            OAuth2AuthenticationToken token) {
       // String role = authControllerAdvice.getRole(token);
        if (!authControllerAdvice.getIsTutor(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } 

        TutorCheckIn tutorCheckIn = new TutorCheckIn(timeSlotAssignmentId, time, date, remarks);

        tutorcheckinRepository.save(tutorCheckIn);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
