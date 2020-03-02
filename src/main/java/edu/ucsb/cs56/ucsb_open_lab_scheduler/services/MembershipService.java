package edu.ucsb.cs56.ucsb_open_lab_scheduler.services;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface MembershipService {

    /** is current logged in user a member but NOT an admin
     * of the github org */
    public boolean isMember(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /** is current logged in user a member of the github org */
    public boolean isAdmin(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /** is current logged in user a tutor but NOT an admin */
    public boolean isTutor(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    /** is current logged in user a member or admin of the
     * github org */
    default public boolean isMemberOrAdminOrTutor(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return isMember(oAuth2AuthenticationToken) || isAdmin(oAuth2AuthenticationToken) || isTutor(oAuth2AuthenticationToken);
    }

    default public String role(OAuth2AuthenticationToken token) {
        if (token==null)
            return "Guest";
        if (isAdmin(token))
           return "Admin";
        if (isMember(token))
           return "Member";
        if (isTutor(token))
            return "Tutor";
        return "Guest";
    }

}