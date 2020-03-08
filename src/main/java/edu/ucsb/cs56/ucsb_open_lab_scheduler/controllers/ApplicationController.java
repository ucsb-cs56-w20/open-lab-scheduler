package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.formbeans.CourseSchedule;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;


@Controller
public class ApplicationController{
    private final CourseOfferingRepository courseOfferingRepository;
    private final TutorAssignmentRepository tutorAssignmentRepository;
    private final TimeSlotAssignmentRepository timeSlotAssignmentRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    Comparator<TimeSlotAssignment>  sortByTimeSlot = (a,b)-> {
        return Integer.compare(a.getTimeSlot().getStartTime(), b.getTimeSlot().getStartTime());
    };

    Comparator<TimeSlotAssignment> sortByDay = (a,b)-> {
        int aDay = convertDayToInt(a.getTimeSlot().getRoomAvailability().getDay());
        int bDay = convertDayToInt(b.getTimeSlot().getRoomAvailability().getDay());
        return Integer.compare(aDay, bDay);
    };

    Comparator<TimeSlotAssignment> sortByTutorLastName = (a,b)-> {
        return (a.getTutor().getLastName()).compareTo(b.getTutor().getLastName());
    };

    Comparator<TimeSlotAssignment> sortByRoom = (a,b)-> {
        return (a.getTimeSlot().getRoomAvailability().getRoom().getName()).compareTo(b.getTimeSlot().getRoomAvailability().getRoom().getName());
    };

    private int convertDayToInt(String day){
        if(day.equals("M")){return 1;}
        else if(day.equals("T")){return 2;}
        else if(day.equals("W")){return 3;}
        else if(day.equals("R")){return 4;}
        else if(day.equals("F")){return 5;}
        else return 6;
    }

    @Autowired
    public ApplicationController(CourseOfferingRepository courseOfferingRepository,
    TutorAssignmentRepository tutorAssignmentRepository, TimeSlotAssignmentRepository timeSlotAssignmentRepository){
        this.courseOfferingRepository = courseOfferingRepository;
        this.tutorAssignmentRepository = tutorAssignmentRepository;
        this.timeSlotAssignmentRepository = timeSlotAssignmentRepository;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("quarter", "F19");
        model.addAttribute("courseId", "CMPSC 16");
        model.addAttribute("uniqueCourseOfferingModel", courseOfferingRepository.findAllUniqueCourses());
        model.addAttribute("uniqueQuartersModel", courseOfferingRepository.findAllUniqueQuarters());
        model.addAttribute("CourseSchedule", new CourseSchedule());
        return "index";
    }

    @GetMapping("/results")
    public String getSchedule(
        @RequestParam(name="quarter", required = true) String quarter,
        @RequestParam(name="courseId", required = true) String courseId,
        Model model) {
        model.addAttribute("quarter", quarter);
        model.addAttribute("courseId", courseId);
        model.addAttribute("CourseSchedule", new CourseSchedule());
        model.addAttribute("uniqueCourseOfferingModel", courseOfferingRepository.findAllUniqueCourses());
        model.addAttribute("uniqueQuartersModel", courseOfferingRepository.findAllUniqueQuarters());
        List<TimeSlotAssignment> timeSlotAssignments =  timeSlotAssignmentRepository.findByCourseOffering(
            courseOfferingRepository.findByQuarterAndCourseId(quarter, courseId));
        java.util.Collections.sort(timeSlotAssignments,sortByDay.thenComparing(sortByRoom).thenComparing(sortByTimeSlot).thenComparing(sortByTutorLastName));
        model.addAttribute("timeSlotAssignments", timeSlotAssignments);
        return "home/results";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        Map<String, String> urls = new HashMap<>();

        // get around an unfortunate limitation of the API
        @SuppressWarnings("unchecked") Iterable<ClientRegistration> iterable = ((Iterable<ClientRegistration>) clientRegistrationRepository);
        iterable.forEach(clientRegistration -> urls.put(clientRegistration.getClientName(),
                "/oauth2/authorization/" + clientRegistration.getRegistrationId()));

        model.addAttribute("urls", urls);
        return "login";
    }
}

