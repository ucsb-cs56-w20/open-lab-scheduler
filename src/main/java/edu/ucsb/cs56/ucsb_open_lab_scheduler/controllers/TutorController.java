package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.CSVToObjectService;

import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("TutorModel")
public class TutorController {
    private static Logger log = LoggerFactory.getLogger(TutorController.class);

    @Autowired
    private AuthControllerAdvice authControllerAdvice;

    @Autowired
    CSVToObjectService<Tutor> csvToObjectService;

    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    TutorAssignmentRepository tutorAssignmentRepository;

    @GetMapping("/tutors")
    public String dashboard(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        HashMap<Long, Tutor> tutorsById = new HashMap<>();
        for(Tutor tutor: tutorRepository.findAll()){
            tutorsById.put(tutor.getId(), tutor);
        }

        Iterable<TutorAssignment> tutorAssignments = tutorAssignmentRepository.findAll();
        for(TutorAssignment tutorAssignment: tutorAssignments){
            if(tutorsById.containsKey(tutorAssignment.getTutor().getId())){
                tutorsById.get(tutorAssignment.getTutor().getId()).incrementNumberOfCoursesAssigned();
            }
        }

        Iterator iterator = tutorsById.entrySet().iterator();
        ArrayList<Tutor> tutors = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)iterator.next();
            tutors.add((Tutor)mapElement.getValue());
        }

        model.addAttribute("TutorModel", tutors);
        return "tutors/tutors";
    }

    @PostMapping("/tutors/upload")
    public String uploadCSV(@RequestParam("csv") MultipartFile csv, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = authControllerAdvice.getRole(token);
        if (!(role.equals("Admin"))) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        try(Reader reader = new InputStreamReader(csv.getInputStream())){
            List<Tutor> tutors = csvToObjectService.parse(reader, Tutor.class);
            tutorRepository.saveAll(tutors);
        }catch(IOException e){
            log.error(e.toString());
        }catch(RuntimeException a){
            redirAttrs.addFlashAttribute("alertDanger", "Please enter a correct csv file.");
            return "redirect:/tutors";
        }
        return "redirect:/tutors";
    }

    @RequestMapping(value="/tutors/active", method=RequestMethod.PUT)
    public ResponseEntity<?> setActive(@RequestParam("tid") long tid, @ModelAttribute("TutorModel") ArrayList<Tutor> tutors, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tutor tutor = tutorRepository.findById(tid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
        tutor.setIsActive(true);
        tutorRepository.save(tutor);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/tutors/{tid}", method=RequestMethod.PUT)
    public ResponseEntity<?> setInactive(@PathVariable("tid") long tid, @ModelAttribute("TutorModel") ArrayList<Tutor> tutors, OAuth2AuthenticationToken token) {
        String role = authControllerAdvice.getRole(token);
        if (!role.equals("Admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tutor tutor = tutorRepository.findById(tid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + tid));
        tutor.setIsActive(false);
        tutorRepository.save(tutor);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tutors/tutors.csv")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "tutors.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Tutor> writer = new StatefulBeanToCsvBuilder<Tutor>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(tutorRepository.findAll().iterator());
    }

    @GetMapping("/tutors/sort")
    public @ResponseBody ArrayList<Tutor> sortTutors(@RequestParam("col") int col, @RequestParam("ascending") boolean ascending, 
            @ModelAttribute("TutorModel") ArrayList<Tutor> tutors) {
        for(Tutor t : tutors) {
            Tutor updatedTutor = tutorRepository.findById(t.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tutor Id:" + t.getId()));
            t.setIsActive(updatedTutor.getIsActive());
        }

        switch(col) {
            case 0:
                // by email, alphabetical
                tutors.sort((t1, t2) -> {
                    if(ascending) {
                        return t1.getEmail().compareToIgnoreCase(t2.getEmail());
                    } else {
                        return t2.getEmail().compareToIgnoreCase(t1.getEmail());
                    }
                });
                break;
            case 1:
                // by first name, alphabetical
                tutors.sort((t1, t2) -> {
                    if(ascending) {
                        return t1.getFirstName().compareToIgnoreCase(t2.getFirstName());
                    } else {
                        return t2.getFirstName().compareToIgnoreCase(t1.getFirstName());
                    }
                });
                break;
            case 2:
                // by last name, alphabetical
                tutors.sort((t1, t2) -> {
                    if(ascending) {
                        return t1.getLastName().compareToIgnoreCase(t2.getLastName());
                    } else {
                        return t2.getLastName().compareToIgnoreCase(t1.getLastName());
                    }
                });
                break;
            case 3:
                // by number of courses assigned, ascending
                tutors.sort((t1, t2) -> {
                    if(ascending) {
                        return Integer.compare(t1.getNumberOfCoursesAssigned(), t2.getNumberOfCoursesAssigned());    
                    } else {
                        return Integer.compare(t2.getNumberOfCoursesAssigned(), t1.getNumberOfCoursesAssigned());
                    }
                });
                break;
            case 4:
                // by active status, active first
                tutors.sort((t1, t2) -> {
                    if(ascending) {
                        return Boolean.compare(t2.getIsActive(), t1.getIsActive()); 
                    } else {
                        return Boolean.compare(t1.getIsActive(), t2.getIsActive());
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("Invalid column number: " + col);
        }

        return tutors;
    }
}
