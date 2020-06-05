package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;


import edu.ucsb.cs56.ucsb_open_lab_scheduler.advice.AuthControllerAdvice;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.*;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.GoogleMembershipService;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.MembershipService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.ui.Model;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utils.OAuthUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthControllerAdvice aca;

    private GoogleMembershipService gms;

    @MockBean
    private AdminRepository ar;

    @MockBean
    private TutorRepository tr;

    @MockBean
    private UserRepository aur;

    @MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private MembershipService ms;

    private Authentication mockAuthentication;

    @Before
    public void setUp() {
        OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@ucsb.edu");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);
        when(ms.isMember((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(true);
    }
    /**
     * This test makes sure unauthenticated users can not access the /admin endpoint
     */
    @Test
    public void testUnauthenticatedAccess() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/admin").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * This test makes sure admins can access the /admin endpoint
     */
    @Test
    public void testAdminAccess() throws Exception {
        when(ms.role(any())).thenReturn("Admin");
        when(aca.getRole(any())).thenReturn("Admin");
        mvc.perform(MockMvcRequestBuilders
                .get("/admin")
                .with(authentication(mockAuthentication))
                .accept(MediaType.TEXT_HTML))
                .andExpect(status()
                        .isOk()
                );
    }
}
