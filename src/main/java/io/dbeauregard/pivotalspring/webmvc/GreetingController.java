package io.dbeauregard.pivotalspring.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    private static Logger log = LoggerFactory.getLogger(GreetingController.class);

    @GetMapping("/greeting")
    public String getGreeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        log.info("/greeting called with principal {}", userDetails.getUsername());
        model.addAttribute("name", name);
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("roles", userDetails.getAuthorities());
        return "greeting";
    }

}
