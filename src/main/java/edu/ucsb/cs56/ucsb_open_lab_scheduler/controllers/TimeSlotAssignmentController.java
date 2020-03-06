package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlot;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotAssignmentRepository;

@Controller
public class TimeSlotAssignmentController {

    Logger logger = LoggerFactory.getLogger(TimeSlotAssignmentController.class);
    
    @Autowired
    CourseOfferingRepository courseOfferingRepository;

    @Autowired
    TimeSlotAssignmentRepository timeSlotAssignmentRepository;

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    private Comparator<TimeSlotAssignment> byRoom = (a, b) -> a.getTimeSlot().getRoomAvailability().getRoom().getName()
            .compareTo(b.getTimeSlot().getRoomAvailability().getRoom().getName());

    private Comparator<TimeSlotAssignment> byCourseOffering = (a, b) -> a.getCourseOffering().getCourseId()
            .compareTo(b.getCourseOffering().getCourseId());
    
    private Comparator<TimeSlotAssignment> byTutorLastName = (a, b) -> a.getTutor().getLastName()
            .compareTo(b.getTutor().getLastName());

    private Comparator<TimeSlotAssignment> byDay = (a, b) -> {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE");
            String dayA = a.getTimeSlot().getRoomAvailability().getDay();
            String dayB = b.getTimeSlot().getRoomAvailability().getDay();
            Date dateA = format.parse(dayA);
            Date dateB = format.parse(dayB);
            if (dateA.equals(dateB)) {
                return dayA.substring(dayA.indexOf(" ") + 1).compareTo(dayB.substring(dayB.indexOf(" ") + 1));
            } else {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(dateA);
                cal2.setTime(dateB);
                return cal1.get(Calendar.DAY_OF_WEEK) - cal2.get(Calendar.DAY_OF_WEEK);
            }
        } catch (Exception e) {
            return -1;
        }
    };

    private Comparator<TimeSlotAssignment> byTime = (a, b) -> Integer.compare(a.getTimeSlot().getStartTime(),
            b.getTimeSlot().getStartTime());

    @GetMapping("/timeSlotAssignment")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        return "/timeSlotAssignment/timeSlotAssignment";
    }

    @GetMapping("/timeSlotAssignment/search/{quarter}")
    public String quarterSearch(@PathVariable("quarter") String quarter, Model model, OAuth2AuthenticationToken token,
            RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        List<CourseOffering> courses = courseOfferingRepository.findByQuarter(quarter);
        List<TimeSlotAssignment> timeSlots = new ArrayList<TimeSlotAssignment>();
        for (CourseOffering course : courses) {
            timeSlots.addAll(timeSlotAssignmentRepository.findByCourseOffering(course));
        }
        java.util.Collections.sort(timeSlots, byRoom.thenComparing(byDay).thenComparing(byTime));
        // 2d array where rows are "unique" time slots (unique in the table) and columns
        // are same time slots w/ different tutors
        ArrayList<ArrayList<TimeSlotAssignment>> uniqueTimeSlots = new ArrayList<ArrayList<TimeSlotAssignment>>();
        for (TimeSlotAssignment tsa : timeSlots) {
            if (uniqueTimeSlots.size() == 0) {
                ArrayList<TimeSlotAssignment> arr = new ArrayList<TimeSlotAssignment>();
                arr.add(tsa);
                uniqueTimeSlots.add(arr);
            } else {
                if (!(uniqueTimeSlots.get(uniqueTimeSlots.size() - 1).get(0).getTimeSlot().getRoomAvailability()
                        .getRoom().getName() == tsa.getTimeSlot().getRoomAvailability().getRoom().getName()
                        && uniqueTimeSlots.get(uniqueTimeSlots.size() - 1).get(0).getTimeSlot().getRoomAvailability()
                                .getDay() == tsa.getTimeSlot().getRoomAvailability().getDay()
                        && uniqueTimeSlots.get(uniqueTimeSlots.size() - 1).get(0).getTimeSlot().getStartTime() == tsa
                                .getTimeSlot().getStartTime()
                        && uniqueTimeSlots.get(uniqueTimeSlots.size() - 1).get(0).getTimeSlot().getEndTime() == tsa
                                .getTimeSlot().getEndTime())) { // if not a duplicate in a table then add
                    ArrayList<TimeSlotAssignment> arr = new ArrayList<TimeSlotAssignment>();
                    arr.add(tsa);
                    uniqueTimeSlots.add(arr);
                } else {
                    uniqueTimeSlots.get(uniqueTimeSlots.size() - 1).add(tsa);
                }
            }
        }
        for(int i = 0; i < uniqueTimeSlots.size(); i++) {
            java.util.Collections.sort(uniqueTimeSlots.get(i), byCourseOffering.thenComparing(byTutorLastName));
        }
        model.addAttribute("uniqueTimeSlots", uniqueTimeSlots);
        model.addAttribute("quarter", quarter);
        return "timeSlotAssignment/search";
    }
}