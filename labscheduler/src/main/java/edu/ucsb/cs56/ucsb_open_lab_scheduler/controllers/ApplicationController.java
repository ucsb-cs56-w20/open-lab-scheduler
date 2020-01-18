package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController{

    @GetMapping("/")
    public String home(){
        return "<h1>bleh<h1>";
    }
}

