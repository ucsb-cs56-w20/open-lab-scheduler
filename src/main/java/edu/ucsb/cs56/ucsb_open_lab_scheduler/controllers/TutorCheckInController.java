
package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

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
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorCheckInRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.ValidEmailService;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TutorCheckInController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    private TutorCheckInRepository tutorcheckinRepository;

    @Value("${app.admin.email}")
    private long timeSlotAssignmentId;

    @Autowired
    public TutorCheckInController(TutorCheckInRepository repo) {
        this.tutorcheckinRepository = repo;
    }

    private void addTutor() {
        if (tutorcheckinRepository.findById(timeSlotAssignmentId).isEmpty()) {
            tutorcheckinRepository.save(new TutorCheckIn());
        }
    }



}