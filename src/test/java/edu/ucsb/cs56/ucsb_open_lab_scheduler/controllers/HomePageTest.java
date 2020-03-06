package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomAvailabilityRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorAssignmentRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.RoomRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
public class HomePageTest {

    @Autowired
	private MockMvc mvc;

	@MockBean
	private AuthControllerAdvice aca;
	
	@MockBean
    private ClientRegistrationRepository crr;

	@MockBean
	private RoomRepository rr;
	
	@MockBean
    private RoomAvailabilityRepository rar;

    @MockBean
    private CourseOfferingRepository cor;

    @MockBean
    private TutorAssignmentRepository tut;

    @Test
    public void getHomePage_ContentType() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }
 
    @Test
    public void getHomePage_hasCorrectTitle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(xpath("//title").exists())
                .andExpect(xpath("//title").string("Open Lab Scheduler"));
    }
}
