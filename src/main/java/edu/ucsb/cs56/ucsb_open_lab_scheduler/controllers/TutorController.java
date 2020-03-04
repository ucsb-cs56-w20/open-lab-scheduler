package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;

@Controller
public class TutorController {
    private static Logger log = LoggerFactory.getLogger(TutorController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    CSVToObjectService<Tutor> csvToObjectService;

    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    TutorAssignmentRepository tutorAssignmentRepository;

    @GetMapping("/tutors")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        HashMap<Long, Tutor> tutorsById = new HashMap<>();
        for(Tutor tutor: tutorRepository.findAll()){
            tutorsById.put(tutor.getId(), tutor);
        }

        Iterable<TutorAssignment> tutorAssignments = tutorAssignmentRepository.findAll();
        for(TutorAssignment tutorAssignment: tutorAssignments){
            if(tutorsById.containsKey(tutorAssignment.getTutor().getId())){
                tutorsById.get(tutorAssignment.getTutor().getId()).incrementNumberOfCoursesAssigned();
            }
        }

        Iterator iterator = tutorsById.entrySet().iterator();
        ArrayList<Tutor> tutors = new ArrayList<>();
        while (iterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)iterator.next(); 
            tutors.add((Tutor)mapElement.getValue()); 
        }

        model.addAttribute("TutorModel", tutors);
        return "tutors/tutors";
    }

    @PostMapping("/tutors/upload")
    public String uploadCSV(@RequestParam("csv") MultipartFile csv, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        try(Reader reader = new InputStreamReader(csv.getInputStream())){
            List<Tutor> tutors = csvToObjectService.parse(reader, Tutor.class);
            tutorRepository.saveAll(tutors);
        }catch(IOException e){
            log.error(e.toString());
        }
        return "redirect:/tutors";
    }

    @PostMapping("/tutors/active")
    public ResponseEntity<?> setActive(@RequestParam("tid") long tid, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tutor tutor = tutorRepository.findById(tid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
        tutor.setIsActive(true);
        tutorRepository.save(tutor);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tutors/{tid}")
    public ResponseEntity<?> setInactive(@PathVariable("tid") long tid, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tutor tutor = tutorRepository.findById(tid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
        tutor.setIsActive(false);
        tutorRepository.save(tutor);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
