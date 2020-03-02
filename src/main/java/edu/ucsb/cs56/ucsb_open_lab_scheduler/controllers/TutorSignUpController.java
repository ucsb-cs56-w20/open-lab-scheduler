package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;

@Controller
public class TutorSignUpController{
    private Logger logger = LoggerFactory.getLogger(TutorAssignmentController.class);
    private final TutorRepository tutorRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final TutorAssignmentRepository tutorAssignmentRepository;
  
    @Autowired
    private AuthControllerAdvice authControllerAdvice;
  
    public TutorSignUpController(TutorAssignmentRepository tutorAssignmentRepository, TutorRepository tutorRepository,
        CourseOfferingRepository courseOfferingRepository) {
      this.tutorAssignmentRepository = tutorAssignmentRepository;
      this.tutorRepository = tutorRepository;
      this.courseOfferingRepository = courseOfferingRepository;
    }
  
    @GetMapping("/tutorSignUp/courseSelect")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
      String role = authControllerAdvice.getRole(token);
      if (!role.equals("Admin")) {
        redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
        return "redirect:/";
      }

      Optional<Tutor> tutor = tutorRepository.findByEmail(authControllerAdvice.getEmail(token));
      if (!tutor.isPresent()) {
        redirAttrs.addFlashAttribute("alertDanger", "Tutor with email " + authControllerAdvice.getEmail(token) + " not found");
        return "redirect:/";
      }
    //   System.out.print(tutor);
      List<TutorAssignment> tutorAssignments = tutorAssignmentRepository.findByTutor(tutor.get());
    //   System.out.println("tutor assignments");
    //   for (TutorAssignment t : tutorAssignments){
    //     System.out.print(t.getCourseOfferingId());
    //     System.out.print(t.getTutorId());
    //     System.out.print(t.getId());
    //     System.out.println();
    //   }
      List<CourseOffering> courseOfferings = new ArrayList<>();

      for (TutorAssignment ta : tutorAssignments){
        //   Optional<CourseOffering> course = courseOfferingRepository.findById(tutorAssignments.get(i).getCourseOfferingId());
        //   if (!course.isPresent()){
        //       redirAttrs.addFlashAttribute("alertDanger", "Course offering with id " + tutorAssignments.get(i).getCourseOfferingId() + " not found");
        //       return "redirect:/";
        //   }
          courseOfferings.add(ta.getCourseOffering());
          System.out.print(ta.getCourseOffering());
      }

      model.addAttribute("courseOffering", courseOfferings);
      return "tutorSignUp/tutorSignUp";
    }
  
    // @GetMapping("/tutorAssignment/courseSelect/{id}")
    // public String manageCourse(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token,
    //     RedirectAttributes redirAttrs) {
    //   String role = authControllerAdvice.getRole(token);
    //   if (!role.equals("Admin")) {
    //     redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
    //     return "redirect:/";
    //   }
  
    //   Optional<CourseOffering> courseOffering = courseOfferingRepository.findById(id);
    //   if (!courseOffering.isPresent()) {
    //     redirAttrs.addFlashAttribute("alertDanger", "Course offering with id " + id + " not found");
    //     return "redirect:/";
  
    //   }
    //   Iterable<Tutor> tutors = tutorRepository.findAll();
    //   List<TutorAssignment> tutorAssignments = tutorAssignmentRepository
    //       .findByCourseOfferingId(courseOffering.get().getId());
  
    //   Predicate<Tutor> shouldBeChecked = tutor -> tutorAssignments.stream()
    //       .anyMatch((ta) -> ta.getTutorId() == tutor.getId());
    //   model.addAttribute("shouldBeChecked", shouldBeChecked);
    //   model.addAttribute("tutors", tutors);
    //   model.addAttribute("courseOffering", courseOffering.get());
  
    //   return "tutorAssignment/manage";
    // }
}