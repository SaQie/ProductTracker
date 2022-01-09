package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/home";
    }

    @PostMapping("/")
    public String redirectToHomePageFromLogin() {
        return "redirect:/home";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/menu")
    public String getMenuPage() {
        return "user_panel";
    }

}
