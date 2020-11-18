package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlot;
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
public class TutorSignUpController{
    private Logger logger = LoggerFactory.getLogger(TutorSignUpController.class);
    private final TutorRepository tutorRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final TutorAssignmentRepository tutorAssignmentRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final TimeSlotAssignmentRepository timeSlotAssignmentRepository;
    private final TimeSlotRepository timeSlotRepository;
  
    @Autowired
    private AuthControllerAdvice authControllerAdvice;
  
    public TutorSignUpController(TutorAssignmentRepository tutorAssignmentRepository, TutorRepository tutorRepository,
        CourseOfferingRepository courseOfferingRepository,RoomAvailabilityRepository roomAvailabilityRepository,
        TimeSlotAssignmentRepository timeSlotAssignmentRepository, TimeSlotRepository timeSlotRepository) {
        this.tutorAssignmentRepository = tutorAssignmentRepository;
        this.tutorRepository = tutorRepository;
        this.courseOfferingRepository = courseOfferingRepository;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.timeSlotAssignmentRepository = timeSlotAssignmentRepository;
        this.timeSlotRepository = timeSlotRepository;
    }
  
    @GetMapping("/tutorSignUp/courseSelect")
    public String signUpTable(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
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
            System.out.print(ta.getCourseOffering());
        }

        model.addAttribute("courseOffering", courseOfferings);
        return "tutorSignUp/tutorSignUp";
    }

    @GetMapping("/tutorSignUp/courseSelect/{id}")
    public String timeSlotAssignMent(@PathVariable("id") long id, Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs){
        String role = authControllerAdvice.getRole(token);
        if (!authControllerAdvice.getIsTutor(token)) {
        redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
        return "redirect:/";
        }

        Optional<CourseOffering> courseOffering = courseOfferingRepository.findById(id);
        if (!courseOffering.isPresent()) {
        redirAttrs.addFlashAttribute("alertDanger", "Course offering with id " + id + " not found");
        return "redirect:/";

        }
        Optional<Tutor> tutor = tutorRepository.findByEmail(authControllerAdvice.getEmail(token));
        if (!tutor.isPresent()) {
            redirAttrs.addFlashAttribute("alertDanger", "Tutor with email " + authControllerAdvice.getEmail(token) + " not found");
            return "redirect:/";
        } 
        
        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository.findByQuarter(courseOffering.get().getQuarter());
        List<TimeSlotAssignment> timeSlotAssignments = timeSlotAssignmentRepository.findByTutorId(tutor.get().getId());
        List<TimeSlot> timeSlots = new ArrayList<>();

        Map<Long, RoomAvailability> roomMap = new HashMap<>();
        for (RoomAvailability ra : roomAvailabilities){
            timeSlots.addAll(timeSlotRepository.findByRoomAvailabilityId(ra.getId()));
            roomMap.put(ra.getId(), ra);
        }

        Predicate<TimeSlot> shouldBeChecked = t -> timeSlotAssignments.stream()
            .anyMatch((ta) -> ta.getTimeSlot() == t && ta.getCourseOffering().equals(courseOffering.get()));
        
    
        model.addAttribute("shouldBeChecked", shouldBeChecked);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("courseOffering", courseOffering.get());
        model.addAttribute("tutor", tutor.get());
        model.addAttribute("roomAvailability", roomMap);


        return "tutorSignUp/timeSlotAssignment";
    }
    @PostMapping("/tutorSignUp/add")
    public ResponseEntity<?> add(@RequestParam("sid") long sid, @RequestParam("tid") long tid,
                                 @RequestParam("cid") long cid, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!authControllerAdvice.getIsTutor(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        TimeSlotAssignment timeSlotAssignment= new TimeSlotAssignment();
        Optional<TimeSlot> ts = timeSlotRepository.findById(sid);
        timeSlotAssignment.setTimeSlot(ts.get());
        Optional<Tutor> tutor = tutorRepository.findById(tid);
        timeSlotAssignment.setTutor(tutor.get());
        Optional<CourseOffering> courseOffering = courseOfferingRepository.findById(cid);
        timeSlotAssignment.setCourseOffering(courseOffering.get());
         
        timeSlotAssignmentRepository.save(timeSlotAssignment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tutorSignUp/{sid}/{tid}/{cid}")
    public ResponseEntity<?> delete(@PathVariable("sid") long sid, @PathVariable("tid") long tid,
                                    @PathVariable("cid") long cid,OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!authControllerAdvice.getIsTutor(token)) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        timeSlotAssignmentRepository.deleteByTimeSlotIdAndTutorIdAndCourseOfferingId(sid, tid, cid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    }
