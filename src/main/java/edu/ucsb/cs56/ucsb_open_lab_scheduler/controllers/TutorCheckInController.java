
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
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorCheckInRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.ValidEmailService;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.validation.Valid;

@Controller
public class TutorCheckInController {

    private Logger logger = LoggerFactory.getLogger(TutorCheckIn.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    private TutorCheckInRepository tutorcheckinRepository;

    // @Value("${app.admin.email}")
    // private long timeSlotAssignmentId;

    @Autowired
    public TutorCheckInController(TutorCheckInRepository repo) {
        this.tutorcheckinRepository = repo;
    }

    @GetMapping("/tutorCheckIn")
    public String tutorcheckin(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Tutor")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        // addTutor();
        model.addAttribute("tutors", tutorcheckinRepository.findAll());
        model.addAttribute("newTutor", new TutorCheckIn());
        return "tutorCheckIn/tutorCheckIn";
    }
    // private void addTutor() {
    //     if (tutorcheckinRepository.findById(timeSlotAssignmentId).isEmpty()) {
    //         tutorcheckinRepository.save(new TutorCheckIn());
    //     }
    // }

    @PostMapping("/tutorCheckIn")
    public String addEntry(@Valid TutorCheckIn tutor, BindingResult result, Model model, RedirectAttributes redirAttrs,
            OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Tutor")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        boolean errors = false;
        if (!errors) {
            tutorcheckinRepository.save(tutor);
            model.addAttribute("newTutor", new TutorCheckIn());
        } else {
            model.addAttribute("newTutor", tutor);
        }
        model.addAttribute("tutors", tutorcheckinRepository.findAll());
        return "redirect:/";
    }

    }