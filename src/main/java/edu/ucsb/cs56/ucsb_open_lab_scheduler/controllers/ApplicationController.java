package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;

@Controller
public class ApplicationController{
    private final RoomRepository roomRepository;
    private final RoomAvailabilityRepository RoomAvailabilityRepository;

    @Autowired
    public ApplicationController(RoomRepository roomRepository, RoomAvailabilityRepository RoomAvailabilityRepository){
        this.roomRepository = roomRepository;
        this.RoomAvailabilityRepository = RoomAvailabilityRepository;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("roomModel", roomRepository.findAll());
        model.addAttribute("RoomAvailabilityModel", RoomAvailabilityRepository.findAll());
        return "index";
    }
}

