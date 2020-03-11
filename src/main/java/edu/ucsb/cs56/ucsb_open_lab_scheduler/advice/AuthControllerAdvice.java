package edu.ucsb.cs56.ucsb_open_lab_scheduler.advice;

import org.springframework.web.bind.annotation.ModelAttribute;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.MembershipService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;


import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.TutorRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import java.util.ArrayList;

@ControllerAdvice
public class AuthControllerAdvice {

    @Autowired   
    private MembershipService membershipService;

    @Autowired
    TutorRepository tutorRepository;

    @ModelAttribute("isLoggedIn")
    public boolean getIsLoggedIn(OAuth2AuthenticationToken token){
        return token != null;
    }

    @ModelAttribute("id")
    public String getUid(OAuth2AuthenticationToken token){
        if (token == null) return "";
        return token.getPrincipal().getAttributes().get("given_name").toString();
    }

    @ModelAttribute("fname")
    public String getFirstName(OAuth2AuthenticationToken token){
        return membershipService.fname(token);
    }

    @ModelAttribute("lname")
    public String getLastName(OAuth2AuthenticationToken token) {
        return membershipService.lname(token);
    }

    @ModelAttribute("name")
    public String getName(OAuth2AuthenticationToken token) {
        return membershipService.name(token);
    }

    @ModelAttribute("email")
    public String getEmail(OAuth2AuthenticationToken token) {
        return membershipService.email(token);
    }

    @ModelAttribute("picture")
    public String getPicture(OAuth2AuthenticationToken token){
        if (token == null) return "";
        return token.getPrincipal().getAttributes().get("picture").toString();
    }

    @ModelAttribute("login")
    public String getLogin(OAuth2AuthenticationToken token){
        if (token == null) return "";
        return token.getName();
    }

    @ModelAttribute("oauth")
    public Object getOauth(OAuth2AuthenticationToken token){
        return token;
    }
    
    @ModelAttribute("isMember")
    public boolean getIsMember(OAuth2AuthenticationToken token){
        return membershipService.isMember(token);
    }

    @ModelAttribute("isInstructor")
    public boolean getIsInstructor(OAuth2AuthenticationToken token){
        return membershipService.isInstructor(token);
    }
    
    @ModelAttribute("isAdmin")
    public boolean getIsAdmin(OAuth2AuthenticationToken token){
        return membershipService.isAdmin(token);
    }

    @ModelAttribute("isTutor")
    public boolean getIsTutor(OAuth2AuthenticationToken token){
        return membershipService.isTutor(token);
    }

    @ModelAttribute("role")
    public String getRole(OAuth2AuthenticationToken token){
        if(getIsAdmin(token)){
            return "Admin";
        }else if(getIsInstructor(token)) {
            return "Instructor";
        }else if(getIsTutor(token)){
            return "Tutor";
        }else if(getIsMember(token)){
            return "Member";
        }else if(getIsLoggedIn(token)){
            return "Guest";
        }

        return "Tutor";
    }

    public List<String> getAdminEmails() {
        return membershipService.getAdminEmails();
    }

}
