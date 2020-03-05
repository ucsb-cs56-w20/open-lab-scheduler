package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.stream.StreamSupport;

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

  @GetMapping("/tutorAssignment/courseSelect")
  public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
    String role = authControllerAdvice.getRole(token);
    if (!(role.equals("Admin"))) {
      redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
      return "redirect:/";
    }
    model.addAttribute("courseOfferings", courseOfferingRepository.findAll());
    return "tutorAssignment/tutorAssignment";
  }

  @GetMapping("/tutorAssignment/courseSelect/{id}")
  public String manageCourse(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token,
      RedirectAttributes redirAttrs) {
    String role = authControllerAdvice.getRole(token);
    String email= (String) token.getPrincipal().getAttributes().get("email");
    List<CourseOffering> instructorCourses=courseOfferingRepository.findByInstructorEmail(email);
    boolean n=false;
    for(int i=0;i<instructorCourses.size();i++){
      if(instructorCourses.get(i).getId()==id)
        n=true;
    }
    if (!((role.equals("Admin")) || (n))) {
      redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
      return "redirect:/";
    }

    Optional<CourseOffering> courseOffering = courseOfferingRepository.findById(id);
    if (!courseOffering.isPresent()) {
      redirAttrs.addFlashAttribute("alertDanger", "Course offering with id " + id + " not found");
      return "redirect:/";

    }
    Iterable<Tutor> tutors = () -> StreamSupport.stream(tutorRepository.findAll().spliterator(), false)
        .filter(tutor -> tutor.getIsActive())
        .iterator();
    List<TutorAssignment> tutorAssignments = tutorAssignmentRepository.findByCourseOffering(courseOffering.get());

    Predicate<Tutor> shouldBeChecked = tutor -> tutorAssignments.stream()
        .filter((ta) -> ta.getTutor().getIsActive())
        .anyMatch((ta) -> ta.getTutor().equals(tutor));

    model.addAttribute("shouldBeChecked", shouldBeChecked);
    model.addAttribute("tutors", tutors);
    model.addAttribute("courseOffering", courseOffering.get());
    
    Predicate<Tutor> shouldBeChecked2 = tutor -> tutorAssignments.stream()
      .filter((ta) -> ta.getIsCourseLead())
      .anyMatch((ta) -> ta.getTutor().equals(tutor));
      
    model.addAttribute("shouldBeChecked2", shouldBeChecked2);

    return "tutorAssignment/manage";
  }
                           
  @PostMapping("/tutorAssignment/add")
  public ResponseEntity<?> add(@RequestParam("cid") long cid, @RequestParam("tid") long tid,  @RequestParam("lead") boolean lead,
                               OAuth2AuthenticationToken token) {
    String role = authControllerAdvice.getRole(token);
    if (!(role.equals("Admin"))) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Tutor tutor = tutorRepository.findById(tid)
        .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
    CourseOffering courseOffering = courseOfferingRepository.findById(cid)
        .orElseThrow(() -> new IllegalArgumentException("Invalid course offering Id:" + cid));

    TutorAssignment tutorAssignment = new TutorAssignment(tutor, courseOffering, lead);
    tutorAssignmentRepository.save(tutorAssignment);

    return new ResponseEntity<>(HttpStatus.OK);
  }
    
    @PostMapping("/tutorAssignment/update")
    public ResponseEntity<?> update(@RequestParam("cid") long cid, @RequestParam("tid") long tid, @RequestParam("lead") boolean lead,
                                 OAuth2AuthenticationToken token) {
      String role = authControllerAdvice.getRole(token);
      if (!role.equals("Admin")) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }

      Tutor tutor = tutorRepository.findById(tid)
          .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
      CourseOffering courseOffering = courseOfferingRepository.findById(cid)
          .orElseThrow(() -> new IllegalArgumentException("Invalid course offering Id:" + cid));
        
        tutorAssignmentRepository.deleteByCourseOfferingIdAndTutorId(cid, tid);
        
        TutorAssignment tutorAssignment = new TutorAssignment(tutor, courseOffering, lead);
        tutorAssignmentRepository.save(tutorAssignment);

      return new ResponseEntity<>(HttpStatus.OK);
    }

  @DeleteMapping("/tutorAssignment/{cid}/{tid}")
  public ResponseEntity<?> delete(@PathVariable("cid") long cid, @PathVariable("tid") long tid,
  OAuth2AuthenticationToken token) {
    String role = authControllerAdvice.getRole(token);
    if (!(role.equals("Admin"))) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    tutorAssignmentRepository.deleteByCourseOfferingIdAndTutorId(cid, tid);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
