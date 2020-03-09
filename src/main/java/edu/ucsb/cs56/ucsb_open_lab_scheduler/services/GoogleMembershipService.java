package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.AdminRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.CourseOfferingRepository;

/**
 * Service object that wraps the UCSB Academic Curriculum API
 */
@Service
public class GoogleMembershipService implements MembershipService {

    private Logger logger = LoggerFactory.getLogger(GoogleMembershipService.class);

    @Value("${app.admin.emails}")
    final private List<String> adminEmails = new ArrayList<String>();

    @Value("${app.member.hosted-domain}")
    private String memberHostedDomain;

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TutorRepository tutorRepository;
    
    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    /**
     * is current logged in user a member but NOT an admin of the google org
     */
    public boolean isMember(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return hasRole(oAuth2AuthenticationToken, "member");
    }

    /** is current logged in user a member of the google org */
    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return hasRole(oAuth2AuthenticationToken, "admin");
    }

    public boolean isTutor(OAuth2AuthenticationToken oauthToken){
        // return hasRole(oAuth2AuthenticationToken, "tutor");
        if (oauthToken == null) {
            return false;
        }
        if (clientService == null) {
            logger.error(String.format("unable to obtain autowired clientService"));
            return false;
        }
        OAuth2AuthorizedClient client = clientService
                .loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        String email = (String) oAuth2User.getAttributes().get("email");
        boolean result = isTutorEmail(email);
        logger.info("Email = " + email + " result = " + result);
        return result;
    }

    public boolean isInstructor(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return hasRole(oAuth2AuthenticationToken, "instructor");
    }


    /**
     * is current logged in user has role
     *
     * @param roleToTest "member" or "admin"
     * @return if the current logged in user has that role
     */
    public boolean hasRole(OAuth2AuthenticationToken oauthToken, String roleToTest) {

        logger.info("adminEmails=[" + adminEmails + "]");

        if (oauthToken == null) {
            return false;
        }
        if (clientService == null) {
            logger.error(String.format("unable to obtain autowired clientService"));
            return false;
        }

        OAuth2AuthorizedClient client = clientService
                .loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
                
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        String email = (String) oAuth2User.getAttributes().get("email");

        // hd is the domain of the email, e.g. ucsb.edu
        String hostedDomain = (String) oAuth2User.getAttributes().get("hd");

        logger.info("email=[" + email + "]");
        logger.info("hostedDomain=" + hostedDomain);

        if (roleToTest.equals("admin") && isAdminEmail(email)) {
            return true;
        }

        if (roleToTest.equals("member") && memberHostedDomain.equals(hostedDomain)) {
            return true;
        }

        if (roleToTest.equals("tutor") && isTutorEmail(email)){
            return true;
        }

        if (roleToTest.equals("instructor") && isInstructorEmail(email)) {
            return true;
        }
        return false;
    }

    private boolean isAdminEmail(String email) {
        return (!adminRepository.findByEmail(email).isEmpty() || (adminEmails.contains(email)));
    }

    public List<String> getAdminEmails() {
        return adminEmails;
    }

    public String name(OAuth2AuthenticationToken token) {
        if(token==null) return "";
        return token.getPrincipal().getAttributes().get("name").toString();
    }

    public String fname(OAuth2AuthenticationToken token) {
        if(token==null) return "";
        return token.getPrincipal().getAttributes().get("given_name").toString();
    }

    public String lname(OAuth2AuthenticationToken token) {
        if(token==null) return "";
        return token.getPrincipal().getAttributes().get("family_name").toString();
    }

    public String email(OAuth2AuthenticationToken token) {
        if(token==null) return "";
        return token.getPrincipal().getAttributes().get("email").toString();
    }

    private boolean isTutorEmail(String email) {
        return (!tutorRepository.findByEmail(email).isEmpty());
    }

    private boolean isInstructorEmail(String email) {
        return (!courseOfferingRepository.findByInstructorEmail(email).isEmpty());
    }
}
