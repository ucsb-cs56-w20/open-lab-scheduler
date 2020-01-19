package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlot;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotRepository;

@Controller
public class ApplicationController{
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public ApplicationController(RoomRepository roomRepository, TimeSlotRepository timeSlotRepository){
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("roomModel", roomRepository.findAll());
        model.addAttribute("timeSlotModel", timeSlotRepository.findAll());
        return "index";
    }
}

