package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Date;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

@Controller
public class TutorScheduleController {    

    private static Logger log = LoggerFactory.getLogger(TutorScheduleController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;


    @GetMapping("/tutorSchedule")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        if (!authControllerAdvice.getIsTutor(token)) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        System.out.println("REDIRECTED");
        return "/tutorSchedule/tutorSchedule";
    }

    @GetMapping("/tutorSchedule/import")
    public String getGoogleCal(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        if (!authControllerAdvice.getIsTutor(token)) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        System.out.println("SOMETHING HAPPENED");
        
        
        // Initialize Calendar service with valid OAuth credentials
//Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), token.getCredentials());
.setApplicationName("applicationName").build();

// Create and initialize a new event (could also retrieve an existing event)
Event event = new Event();
event.setICalUID("originalUID");

Event.Organizer organizer = new Event.Organizer();
organizer.setEmail("organizerEmail");
organizer.setDisplayName("organizerDisplayName");
event.setOrganizer(organizer);

ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
attendees.add(new EventAttendee().setEmail("attendeeEmail"));
// ...
event.setAttendees(attendees);

Date startDate = new Date();
Date endDate = new Date(startDate.getTime() + 3600000);
DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
event.setStart(new EventDateTime().setDateTime(start));
DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
event.setEnd(new EventDateTime().setDateTime(end));

// Import the event into a calendar
Event importedEvent = service.events().calendarImport("primary", event).execute();

System.out.println(importedEvent.getId());

        return "/tutorSchedule/tutorSchedule";
    }


}
