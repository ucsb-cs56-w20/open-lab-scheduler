package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

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
import java.util.List;


import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToTutorService;

@Controller
public class TutorController {
    private static Logger log = LoggerFactory.getLogger(TutorController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    CSVToTutorService csvtt;

    @Autowired
    TutorRepository tutorRepository;

    @GetMapping("/tutors")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("TutorModel", tutorRepository.findAll());
        return "tutors/tutors";
    }

    @PostMapping("/tutors/upload")
    public String uploadCSV(@RequestParam("csv") MultipartFile csv, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        try(Reader reader = new InputStreamReader(csv.getInputStream())){
            List<Tutor> tutors = csvtt.parse(reader);
            tutorRepository.saveAll(tutors);
        }catch(IOException e){
            log.error(e.toString());
        }
        return "redirect:/tutors";
    }
}
