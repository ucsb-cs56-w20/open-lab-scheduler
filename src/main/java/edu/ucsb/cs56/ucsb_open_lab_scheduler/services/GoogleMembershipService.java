package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



/**
 * Service object that wraps the UCSB Academic Curriculum API
 */
@Service
public class GoogleMembershipService implements MembershipService {

    private Logger logger = LoggerFactory.getLogger(GoogleMembershipService.class);

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.member.hosted-domain}")
    private String memberHostedDomain;

    @Autowired
    private OAuth2AuthorizedClientService clientService;

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

    /**
     * is current logged in user has role
     *
     * @param roleToTest "member" or "admin"
     * @return if the current logged in user has that role
     */

    public boolean hasRole(OAuth2AuthenticationToken oauthToken, String roleToTest) {
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
        String hd = (String) oAuth2User.getAttributes().get("hd");

        if (hd == null) {
            return false;
        }

        if (roleToTest.equals("member") && hd.equals(memberHostedDomain)) {
            return true;
        }

        if (roleToTest.equals("admin") && email.equals(adminEmail)) {
            return true;
        }

        return false;
    }

}
