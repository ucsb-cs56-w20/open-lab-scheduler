package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.formbeans.TutorDetailsForm;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TutorDetailsController {
    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    TutorController tutorController;

    @Autowired
    CSVToObjectService<Tutor> csvToObjectService;

    @Autowired
    TutorRepository tutorRepository;

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("tutor", new Tutor());
        return "tutors/details";
    }

    @GetMapping("/tutors/{id}")
    public String tutorDetailsById(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs, @PathVariable(value="id") long id) {

        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        Optional<Tutor> tutor = tutorRepository.findById(id);
        if (!tutor.isPresent()) {
            throw new RuntimeException("Tutor does not exist");
        }

        model.addAttribute("tutor", new TutorDetailsForm(tutor.get()));
        return "tutors/details";

    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute("tutor") TutorDetailsForm tutorDetails, Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        if (tutorDetails.getId() == 0) {
            // TODO: create new tutor
        } else {
            // update existing tutor
            Optional<Tutor> tutor = tutorRepository.findById(tutorDetails.getId());
            if (!tutor.isPresent()) {
                throw new RuntimeException("Tutor does not exist");
            }
            // update vals on Tutor with given form vals
            tutorDetails.updateTutor(tutor.get());
            tutorRepository.save(tutor.get());
        }
        return tutorController.dashboard(model, token, redirAttrs);
    }
}
