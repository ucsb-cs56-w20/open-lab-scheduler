package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;

@Controller
public class TutorProfileController{
    private Logger logger = LoggerFactory.getLogger(TutorSignUpController.class);
    private final TutorRepository tutorRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final TutorAssignmentRepository tutorAssignmentRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final TimeSlotAssignmentRepository timeSlotAssignmentRepository;
    private final TimeSlotRepository timeSlotRepository;
  
    @Autowired
    private AuthControllerAdvice authControllerAdvice;
  
    public TutorProfileController(TutorAssignmentRepository tutorAssignmentRepository, TutorRepository tutorRepository,
        CourseOfferingRepository courseOfferingRepository,RoomAvailabilityRepository roomAvailabilityRepository,
        TimeSlotAssignmentRepository timeSlotAssignmentRepository, TimeSlotRepository timeSlotRepository) {
      this.tutorAssignmentRepository = tutorAssignmentRepository;
      this.tutorRepository = tutorRepository;
      this.courseOfferingRepository = courseOfferingRepository;
      this.roomAvailabilityRepository = roomAvailabilityRepository;
      this.timeSlotAssignmentRepository = timeSlotAssignmentRepository;
      this.timeSlotRepository = timeSlotRepository;
    }
  
    @GetMapping("/tutorProfile")
    public String currentCourses(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {

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

        List<TutorAssignment> tutorAssignments = tutorAssignmentRepository.findByTutorAndCourseOfferingQuarter(tutor.get(), "W20");

        List<CourseOffering> courseOfferings = new ArrayList<>();

        for (TutorAssignment ta : tutorAssignments){
            courseOfferings.add(ta.getCourseOffering());
        }

        List<TimeSlotAssignment> timeSlotAssignments = timeSlotAssignmentRepository.findByTutor(tutor.get());

        model.addAttribute("CourseOfferings", courseOfferings);
        // model.addAttribute("timeSlotAssignments", timeSlotAssignments);

        return "tutorProfile/currentCourses";
    }

    @GetMapping("/tutorProfile/allCourses")
    public String allCourses(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
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

        List<TutorAssignment> tutorAssignments = tutorAssignmentRepository.findByTutor(tutor.get());

        List<CourseOffering> courseOfferings = new ArrayList<>();

        for (TutorAssignment ta : tutorAssignments){
            courseOfferings.add(ta.getCourseOffering());
        }

        List<TimeSlotAssignment> timeSlotAssignments = timeSlotAssignmentRepository.findByTutor(tutor.get());

        model.addAttribute("CourseOfferings", courseOfferings);
        // model.addAttribute("timeSlotAssignments", timeSlotAssignments);

        return "tutorProfile/allCourses";
    }

}