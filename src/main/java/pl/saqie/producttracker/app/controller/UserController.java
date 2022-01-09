package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.error.user.PasswordNotMatchException;
import pl.saqie.producttracker.app.services.user.UserService;
import pl.saqie.producttracker.app.services.user.dto.ChangePasswordDto;
import pl.saqie.producttracker.app.services.user.dto.DeleteAccountDto;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user_details")
    public String getUserDetailsPanel(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(user);
        return "user_account";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete_account")
    public String getUserDeletePage(Model model) {
        model.addAttribute(new DeleteAccountDto());
        return "user_delete_account";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete_account")
    public String processDeleteUser(@ModelAttribute @Valid DeleteAccountDto deleteDto, BindingResult bindingResult, @AuthenticationPrincipal User user, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.processDeleteUserAccount(deleteDto, user);
                return "redirect:/logout";
            } catch (PasswordNotMatchException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "user_delete_account";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/change_password")
    public String getChangePasswordPage(Model model) {
        model.addAttribute(new ChangePasswordDto());
        return "user_change_password";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change_password")
    public String processChangePassword(@ModelAttribute @Valid ChangePasswordDto changePasswordDto, BindingResult bindingResult, @AuthenticationPrincipal User user, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.processChangeUserPassword(changePasswordDto, user);
                model.addAttribute("changePasswordSuccess", "Pomyślnie zmieniono hasło.");
            } catch (PasswordNotMatchException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "user_change_password";
    }

}
