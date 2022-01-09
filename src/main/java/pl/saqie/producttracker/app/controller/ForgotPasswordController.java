package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.saqie.producttracker.app.error.user.EmailNotExistsException;
import pl.saqie.producttracker.app.error.user.InvalidTokenException;
import pl.saqie.producttracker.app.error.user.PasswordNotMatchException;
import pl.saqie.producttracker.app.services.user.UserService;
import pl.saqie.producttracker.app.services.user.dto.ForgotPasswordDto;
import pl.saqie.producttracker.app.services.user.dto.ResetPasswordDto;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@AllArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;

    @GetMapping("/forgot_password")
    public String getForgotPasswordPage(Model model) {
        model.addAttribute(new ForgotPasswordDto());
        return "user_forgot_password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(@ModelAttribute @Valid ForgotPasswordDto forgotPasswordDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.processForgotPassword(forgotPasswordDto);
                model.addAttribute("forgotPasswordSuccess", "Wysłaliśmy na podany adres E-Mail wiadomość z linkiem do zresetowania hasła.");
            } catch (EmailNotExistsException e) {
                model.addAttribute("error", e.getMessage());
            } catch (MessagingException | UnsupportedEncodingException exception) {
                model.addAttribute("error", "Nie udało nam się wysłać maila do zresetowania hasła, skontaktuj się z administratorem");
            }
        }
        return "user_forgot_password";
    }

    @GetMapping("/password_reset")
    public String getResetPasswordPage(@RequestParam(name = "uuid", defaultValue = "error") String uuid, Model model) {
        try {
            ResetPasswordDto resetPasswordDto = userService.processCheckForgotPasswordToken(uuid);
            model.addAttribute(resetPasswordDto);
        } catch (InvalidTokenException e) {
            model.addAttribute("foundTokenError", e.getMessage());
        }
        return "user_reset_password";
    }


    @PostMapping("/password_reset")
    public String processResetUserPassword(@ModelAttribute @Valid ResetPasswordDto resetPasswordDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.processResetUserPassword(resetPasswordDto);
                model.addAttribute("resetPasswordSuccess", "Pomyślnie zresetowano hasło, przejdz do sekcji logowania i zaloguj się używając nowego hasła.");
            } catch (InvalidTokenException | PasswordNotMatchException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "user_reset_password";
    }
}
