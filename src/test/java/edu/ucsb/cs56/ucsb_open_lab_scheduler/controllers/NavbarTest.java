package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.*;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.MembershipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
public class NavbarTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseOfferingRepository cor;

    @MockBean
    private TutorAssignmentRepository tar;

    @MockBean
    private TimeSlotAssignmentRepository tsar;

    @MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private TutorRepository tr;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private MembershipService ms;

    @MockBean
    private UserRepository aur;

    @MockBean
    private AdminRepository ar;


    @Test
    public void testGuestNavbar() throws Exception {
        when(aca.getFirstName(any())).thenReturn("Joe");
        when(aca.getLastName(any())).thenReturn("Gaucho");
        when(aca.getEmail(any())).thenReturn("joegaucho@ucsb.edu");
        when(aca.getRole(any())).thenReturn("Guest");
        when(aca.getIsLoggedIn(any())).thenReturn(true);
        // Now check that the name in the header is Joe
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[3]/li[1]/a")
                        .string("Joe"));

        // check role in header is (Guest)
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[3]/li[2]")
                        .string("(Guest)"));

        // Check login button is NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[3]/li/form/button")
                        .doesNotExist());

        // Make sure all admin buttons are NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[1]/a")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[2]/a")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[3]/a")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[4]/a")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[5]/a")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[6]/a")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[7]/a")
                        .doesNotExist());
    }
    @Test
    public void testAdminNavbar() throws Exception {
        when(aca.getFirstName(any())).thenReturn("Joe");
        when(aca.getLastName(any())).thenReturn("Gaucho");
        when(aca.getEmail(any())).thenReturn("joegaucho@ucsb.edu");
        when(aca.getIsAdmin(any())).thenReturn(true);
        when(aca.getRole(any())).thenReturn("Admin");
        when(aca.getIsLoggedIn(any())).thenReturn(true);

        // Now check that the name in the header is Joe
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[3]/li[1]/a")
                        .string("Joe"));

        // check role in header is (Admin)
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[3]/li[2]")
                        .string("(Admin)"));

        // Check login button is NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[3]/li/form/button")
                        .doesNotExist());

        // Make sure all admin buttons are present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[1]/a")
                        .exists())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[2]/a")
                        .exists())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[3]/a")
                        .exists())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[4]/a")
                        .exists())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[5]/a")
                        .exists())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[6]/a")
                        .exists())
                .andExpect(xpath("/html/body/div/nav/div/ul[1]/li[7]/a")
                        .exists());
    }
}
