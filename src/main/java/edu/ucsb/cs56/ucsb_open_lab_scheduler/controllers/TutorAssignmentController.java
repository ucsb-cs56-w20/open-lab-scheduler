package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Optional;

@Controller
public class TutorAssignmentController {

  private Logger logger = LoggerFactory.getLogger(TutorAssignmentController.class);
  private final TutorRepository tutorRepository;
  private final CourseOfferingRepository courseOfferingRepository;
  private final TutorAssignmentRepository tutorAssignmentRepository;

  @Autowired
  private AuthControllerAdvice authControllerAdvice;

  public TutorAssignmentController(TutorAssignmentRepository tutorAssignmentRepository, TutorRepository tutorRepository,
      CourseOfferingRepository courseOfferingRepository) {
    this.tutorAssignmentRepository = tutorAssignmentRepository;
    this.tutorRepository = tutorRepository;
    this.courseOfferingRepository = courseOfferingRepository;
  }

  @GetMapping("/tutorAssignment")
  public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
    String role = authControllerAdvice.getRole(token);
    if (!role.equals("Admin")) {
      redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
      return "redirect:/";
    }
    model.addAttribute("courseOfferings", courseOfferingRepository.findAll());
    return "tutorAssignment";
  }

  @GetMapping("/tutorAssignment/{id}")
  public String manageCourse(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token,
      RedirectAttributes redirAttrs) {
    String role = authControllerAdvice.getRole(token);
    if (!role.equals("Admin")) {
      redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
      return "redirect:/";
    }

    Optional<CourseOffering> courseOffering = courseOfferingRepository.findById(id);
    if (!courseOffering.isPresent()) {
      redirAttrs.addFlashAttribute("alertDanger", "Course offering with id " + id + " not found");
      return "redirect:/";

    }

    model.addAttribute("courseOffering", courseOffering.get());

    return "tutorAssignment/manage";
  }

  // @PostMapping("/tutorAssignment/add")
  // public String add(@RequestParam(name = "cid") long cid, @RequestParam(name =
  // "tid") long tid, Model model) {
  // Tutor tutor = tutorRepository.findById(tid)
  // .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
  // CourseOffering courseOffering = courseOfferingRepository.findById(cid)
  // .orElseThrow(() -> new IllegalArgumentException("Invalid course offering Id:"
  // + cid));

  // TutorAssignment tutorAssignment = new TutorAssignment(tutor, courseOffering);
  // tutorAssignmentRepository.save(tutorAssignment);
  // model.addAttribute("tutorAssignment", tutorAssignmentRepository.findAll());
  // return "tutorAssignment";
  // }

  // @GetMapping("/tutorAssignment/delete/{id}")
  // public String delete(@PathVariable("id") long id, Model model) {
  // TutorAssignment tutorAssignment = tutorAssignmentRepository.findById(id)
  // .orElseThrow(() -> new IllegalArgumentException("Invalid tutor assignment
  // Id:" + id));
  // tutorAssignmentRepository.delete(tutorAssignment);
  // return "tutorAssignment";
  // }

}