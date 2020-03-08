package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
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
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TimeSlotAssignmentRepository;

@Controller
public class TimeSlotAssignmentController {

    Logger logger = LoggerFactory.getLogger(TimeSlotAssignmentController.class);
    
    @Autowired
    CourseOfferingRepository courseOfferingRepository;

    @Autowired
    TimeSlotAssignmentRepository timeSlotAssignmentRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    private Comparator<TimeSlotAssignment> byRoom = (a, b) -> a.getTimeSlot().getRoomAvailability().getRoom().getName()
            .compareTo(b.getTimeSlot().getRoomAvailability().getRoom().getName());

    private Comparator<TimeSlotAssignment> byCourseOffering = (a, b) -> a.getCourseOffering().getCourseId()
            .compareTo(b.getCourseOffering().getCourseId());

    private Comparator<CourseOffering> byCourseId = (a, b) -> a.getCourseId()
            .compareTo(b.getCourseId());
    
    private Comparator<TimeSlotAssignment> byTutorLastName = (a, b) -> a.getTutor().getLastName()
            .compareTo(b.getTutor().getLastName());

    private Comparator<TimeSlotAssignment> byDay = (a, b) -> {
        int dayA = -1;
        int dayB = -1;
        switch(a.getTimeSlot().getRoomAvailability().getDay()) {
            case "M":
                dayA = 0;
                break;
            case "T":
                dayA = 1;
                break;
            case "W":
                dayA = 2;
                break;
            case "R":
                dayA = 3;
                break;
            case "F":
                dayA = 4;
                break;
        }
        switch(b.getTimeSlot().getRoomAvailability().getDay()) {
            case "M":
                dayB = 0;
                break;
            case "T":
                dayB = 1;
                break;
            case "W":
                dayB = 2;
                break;
            case "R":
                dayB = 3;
                break;
            case "F":
                dayB = 4;
                break;
        }
        return Integer.compare(dayA, dayB);
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
        for(TimeSlotAssignment time : timeSlots) {
            logger.info(time.getId() + "");
        }
        
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

        java.util.Collections.sort(courses, byCourseId);
        ArrayList<CourseOffering> uniqueCourses = new ArrayList<CourseOffering>();
        for(int i = 0; i < courses.size(); i++) {
            if(i == 0) {
                uniqueCourses.add(courses.get(i));
            } else {
                if(!uniqueCourses.get(uniqueCourses.size() - 1).getCourseId().equals(courses.get(i).getCourseId())) {
                    uniqueCourses.add(courses.get(i));
                }
            }
        }

        ArrayList<Room> uniqueRooms = new ArrayList<Room>();
        for(int i = 0; i < uniqueTimeSlots.size(); i++) {
            if(i == 0) {
                uniqueRooms.add(roomRepository.findByName(uniqueTimeSlots.get(i).get(0).getTimeSlot().getRoomAvailability().getRoom().getName()));
            } else {
                if(!uniqueRooms.get(uniqueRooms.size() - 1).getName().equals(uniqueTimeSlots.get(i).get(0).getTimeSlot().getRoomAvailability().getRoom().getName())) {
                    uniqueRooms.add(roomRepository.findByName(uniqueTimeSlots.get(i).get(0).getTimeSlot().getRoomAvailability().getRoom().getName()));
                }
            }
        }

        ArrayList<ArrayList<Integer>> numTutorsPerRoom = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < uniqueTimeSlots.size(); i++) {
            if(i == 0) {
                ArrayList<Integer> numTutors = new ArrayList<Integer>();
                numTutors.add(uniqueTimeSlots.get(0).size());
                numTutorsPerRoom.add(numTutors);
            } else if(i != 0 && uniqueTimeSlots.get(i).get(0).getTimeSlot().getRoomAvailability().getRoom().getName().equals(uniqueTimeSlots.get(i - 1).get(0).getTimeSlot().getRoomAvailability().getRoom().getName())) {
                numTutorsPerRoom.get(numTutorsPerRoom.size() - 1).set(0, numTutorsPerRoom.get(numTutorsPerRoom.size() - 1).get(0) + uniqueTimeSlots.get(i).size());
            } else {
                ArrayList<Integer> numTutors = new ArrayList<Integer>();
                numTutors.add(uniqueTimeSlots.get(i).size());
                numTutorsPerRoom.add(numTutors);
            }
            String currentCourseOffering = "";
            int counter = 0;
            for(int j = 0; j < uniqueTimeSlots.get(i).size(); j++) {
                if(j == 0) {
                    currentCourseOffering = uniqueTimeSlots.get(i).get(j).getCourseOffering().getCourseId();
                    counter++;
                } else {
                    if(uniqueTimeSlots.get(numTutorsPerRoom.size() - 1).get(j).getCourseOffering().getCourseId().equals(currentCourseOffering)) {
                        counter++;
                    } else {
                        numTutorsPerRoom.get(numTutorsPerRoom.size() - 1).add(counter);
                        currentCourseOffering = uniqueTimeSlots.get(i).get(j).getCourseOffering().getCourseId();
                        counter = 1;
                    }
                }
                numTutorsPerRoom.get(numTutorsPerRoom.size() - 1).add(counter);
            }
            logger.error("" + numTutorsPerRoom.size());
            if(numTutorsPerRoom.get(numTutorsPerRoom.size() - 1).size() < uniqueCourses.size()) {
                numTutorsPerRoom.get(numTutorsPerRoom.size() - 1).add(0);
            }
        }



        model.addAttribute("tutorsPerRoom", numTutorsPerRoom);
        model.addAttribute("uniqueTimeSlots", uniqueTimeSlots);
        model.addAttribute("uniqueCourses", uniqueCourses);
        model.addAttribute("uniqueRooms", uniqueRooms);
        model.addAttribute("quarter", quarter);
        return "timeSlotAssignment/search";
    }
}