package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.*;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.GoogleMembershipService;
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
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private GoogleMembershipService gms;

    @MockBean
    private MembershipService ms;

    @MockBean
    private UserRepository aur;

    @MockBean
    private AdminRepository ar;


    /**
     * This tests the authenticated "guest" user to make sure there is no Google login button in the menu bar,
     * but instead has the user's name with (Guest) and the proper logout button.
     * Also tests to make sure Guests do not have links to admin/student resources
     * @throws Exception
     */
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
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li[1]/a")
                        .string("Joe"));

        // check role in header is (Guest)
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li[2]")
                        .string("(Guest)"));

        // Check login button is NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li/form[@class='form-inline my-2 my-lg-0']/button[@class='navbar-btn']")
                        .doesNotExist());

        // Make sure all admin buttons are NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][0]/a[@id='navbarDropdown']")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][1]/a[@id='navbarDropdown']")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][2]/a[@id='navbarDropdown']")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][3]/a[@id='navbarDropdown']")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][4]/a[@id='navbarDropdown']")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][5]/a[@id='navbarDropdown']")
                        .doesNotExist())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][6]/a[@id='navbarDropdown']")
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
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li[1]/a")
                        .string("Joe"));

        // check role in header is (Admin)
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li[2]")
                        .string("(Admin)"));

        // Check login button is NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li/form[@class='form-inline my-2 my-lg-0']/button[@class='navbar-btn']")
                        .doesNotExist());

        // Make sure all admin buttons are NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][1]/a[@id='navbarDropdown']")
                        .exists())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][2]/a[@id='navbarDropdown']")
                        .exists())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][3]/a[@id='navbarDropdown']")
                        .exists())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][4]/a[@id='navbarDropdown']")
                        .exists())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][5]/a[@id='navbarDropdown']")
                        .exists())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][6]/a[@id='navbarDropdown']")
                        .exists())
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='navbar-nav mr-auto mt-2 mt-lg-0']/li[@class='nav-item '][7]/a[@id='navbarDropdown']")
                        .exists());
    }
}
