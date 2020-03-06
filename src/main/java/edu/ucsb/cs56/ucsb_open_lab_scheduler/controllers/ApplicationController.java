package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.formbeans.courseSchedule;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ApplicationController{
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final TutorAssignmentRepository tutorAssignmentRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public ApplicationController(RoomAvailabilityRepository roomAvailabilityRepository, CourseOfferingRepository courseOfferingRepository,
    TutorAssignmentRepository tutorAssignmentRepository){
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.courseOfferingRepository = courseOfferingRepository;
        this.tutorAssignmentRepository = tutorAssignmentRepository;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("roomAvailabilityModel", roomAvailabilityRepository.findAll());
        model.addAttribute("uniqueCourseOfferingModel", courseOfferingRepository.findAllUniqueCourses());
        model.addAttribute("uniqueQuartersModel", courseOfferingRepository.findAllUniqueQuarters());
        return "index";
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

    @GetMapping("/display")
    public String home(Model model, courseSchedule courseSchedule) {
        String q = courseSchedule.getQuarter();
        String c = courseSchedule.getCourseId();
        model.addAttribute("uniqueCourseOfferingModel", courseOfferingRepository.findAllUniqueCourses());
        model.addAttribute("uniqueQuartersModel", courseOfferingRepository.findAllUniqueQuarters());
        model.addAttribute("tutorAssignmentModel", tutorAssignmentRepository.findByCourseOffering(courseOfferingRepository.findByQuarterAndCourseId(q,c)));
        return "display";
    }
}

