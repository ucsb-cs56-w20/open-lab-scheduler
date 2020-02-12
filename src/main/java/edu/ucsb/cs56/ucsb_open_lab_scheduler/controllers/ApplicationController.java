package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;

@Controller
public class ApplicationController{
    private final RoomAvailabilityRepository RoomAvailabilityRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public ApplicationController(RoomAvailabilityRepository RoomAvailabilityRepository){
        this.RoomAvailabilityRepository = RoomAvailabilityRepository;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("RoomAvailabilityModel", RoomAvailabilityRepository.findAll());
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
}

