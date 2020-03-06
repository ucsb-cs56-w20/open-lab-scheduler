package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.User;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.UsersToCSV;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVDownloadController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/usersCSV")
    public void downloadCSV(HttpServletResponse response) throws IOException{
        String filename = "Users.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        List<User> users =  (List<User>) userRepository.findAll();

        UsersToCSV.writeUsers(response.getWriter(), users);
    }
}