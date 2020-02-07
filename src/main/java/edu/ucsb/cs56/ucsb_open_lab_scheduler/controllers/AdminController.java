package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;

@Controller
public class AdminController{
   
    @Autowired
    private AuthControllerAdvice authControllerAdvice;

  
    // @Autowired
    // public ApplicationController(RoomRepository roomRepository, RoomAvailabilityRepository RoomAvailabilityRepository){
    //     this.roomRepository = roomRepository;
    //     this.RoomAvailabilityRepository = RoomAvailabilityRepository;
    // }

    @GetMapping("/admin")
    public String admin(OAuth2AuthenticationToken token, RedirectAttributes redirAttrs){
       
        String role = authControllerAdvice.getRole(token);
        if (! role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        return "admin/create";
    }

    
}

