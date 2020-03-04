package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.formbeans.TutorDetailsForm;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class TutorDetailsController {
    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    private TutorAssignmentRepository tutorAssignmentRepository;

    @Autowired
    TutorController tutorController;

    @Autowired
    CSVToObjectService<Tutor> csvToObjectService;

    @Autowired
    TutorRepository tutorRepository;

    @GetMapping("/tutors/add")
    public String addTutor(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {

        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        model.addAttribute("tutor", new TutorDetailsForm());
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

    @GetMapping("/tutors/{id}/delete")
    public void deleteTutor(HttpServletResponse httpServletResponse,
                            OAuth2AuthenticationToken token, RedirectAttributes redirAttrs,
                            @PathVariable(value="id") long id) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return;
        }
        Optional<Tutor> tutor = tutorRepository.findById(id);
        if (!tutor.isPresent()) {
            throw new RuntimeException("Tutor does not exist");
        }
        tutorAssignmentRepository.deleteByTutorId(id);
        tutorRepository.delete(tutor.get());
        //redirect
        httpServletResponse.setHeader("Location", "/tutors");
        httpServletResponse.setStatus(302);
    }

    @PostMapping("/tutor/updatedetails")
    public void greetingSubmit(HttpServletResponse httpServletResponse,
                               @ModelAttribute("tutor") TutorDetailsForm tutorDetails,
                               Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return;
        }
        if (tutorDetails.getId() == 0) {
            Tutor t = new Tutor();
            tutorDetails.updateTutor(t);
            tutorRepository.save(t);
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
        //redirect
        httpServletResponse.setHeader("Location", "/tutors");
        httpServletResponse.setStatus(302);
    }
}
