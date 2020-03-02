package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Controller
public class TutorViewController {
    private static Logger log = LoggerFactory.getLogger(TutorViewController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;
    /*
    @Autowired
    CSVToObjectService<Tutor> csvToObjectService;

    @Autowired
    TutorRepository tutorRepository;*/

    @GetMapping("/tutorView")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Tutor"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        /*model.addAttribute("TutorModel", tutorRepository.findAll());
        return "tutors";*/
        return "tutorView";
    }
}