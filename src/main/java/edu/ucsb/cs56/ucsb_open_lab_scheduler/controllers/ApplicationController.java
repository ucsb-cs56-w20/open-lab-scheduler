package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ApplicationController{

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public ApplicationController(RoomAvailabilityRepository roomAvailabilityRepository){
        this.roomAvailabilityRepository = roomAvailabilityRepository;
    }

    @GetMapping("/")
    public String home(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(oAuth2AuthenticationToken);
        if(role.equals("NotDomain")){
            // redirAttrs.addFlashAttribute("alertDanger", "Warning: Guests have limited access to this website. Please log in with a @ucsb.edu email.");
        }

        model.addAttribute("roomAvailabilityModel", roomAvailabilityRepository.findAll());
        return "index"; // returns a view to display
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        String role = authControllerAdvice.getRole(oAuth2AuthenticationToken);
        Map<String, String> urls = new HashMap<>();

        // if (role.equals("NotDomain")) {
        //     // redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
        //     return "error"; // custom error page prompting user to relog in
        // }
        
        // get around an unfortunate limitation of the API
        @SuppressWarnings("unchecked") Iterable<ClientRegistration> iterable = ((Iterable<ClientRegistration>) clientRegistrationRepository);
        iterable.forEach(clientRegistration -> urls.put(clientRegistration.getClientName(),
                "/oauth2/authorization/" + clientRegistration.getRegistrationId()));

        // String role = authControllerAdvice.getRole(oAuth2AuthenticationToken);
        // if(role.equals("NotDomain"))
        //     return "error";

        model.addAttribute("urls", urls);
        return "login"; // custom error page, placeholder for now
    }
}
