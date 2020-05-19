package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;


import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.AppUser;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories.UserRepository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.services.MembershipService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class userController {

    private Logger logger = LoggerFactory.getLogger(userController.class);

    @Autowired
    private MembershipService ms;

    private UserRepository userRepository;

    @Autowired
    public userController(UserRepository repo) {
        this.userRepository = repo;
    }

    @GetMapping("/users")
    public String users(Model model, OAuth2AuthenticationToken token,
            RedirectAttributes redirAttrs) {
        String role = ms.role(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger",
                    "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("users", userRepository.findAll());
        return "users/index";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id, Model model,
            RedirectAttributes redirAttrs, OAuth2AuthenticationToken token) {
        String role = ms.role(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger",
                    "You do not have permission to access that page");
            return "redirect:/";
        }

        Optional<AppUser> optionaluser = userRepository.findById(id);
        if (!optionaluser.isPresent()) {
            redirAttrs.addFlashAttribute("alertDanger", "User with that id does not exist.");
        } else {
            AppUser user = optionaluser.get();
            String email = user.getEmail();
            String curEmail = ms.email(token);

            if (email.equals(curEmail)) {
                redirAttrs.addFlashAttribute("alertDanger", "Cannot delete the current user");
            } else {
                userRepository.delete(user);
                redirAttrs.addFlashAttribute("alertSuccess", "User " + email + "successfully deleted.");
            }
        }
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/users";
    }

}