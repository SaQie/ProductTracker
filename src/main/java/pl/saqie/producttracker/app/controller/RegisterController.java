package pl.saqie.producttracker.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.saqie.producttracker.app.config.AppProperties;
import pl.saqie.producttracker.app.error.user.InvalidTokenException;
import pl.saqie.producttracker.app.error.user.PasswordNotMatchException;
import pl.saqie.producttracker.app.error.user.UserAlreadyExistsException;
import pl.saqie.producttracker.app.services.user.UserService;
import pl.saqie.producttracker.app.services.user.dto.RegisterDto;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@AllArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final AppProperties appProperties;

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute(new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegisterUser(@ModelAttribute @Valid RegisterDto registerDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.processSaveNewUser(registerDto);
                model.addAttribute("registerSuccesfull", "Pomyślnie założyłeś konto w aplikacji, pamiętaj aby potwierdzić swój adres E-Mail. Token aktywacyjny będzie ważny przez " + appProperties.getActivationTokenExpiredInMinutes() + " minut");
            } catch (UserAlreadyExistsException | PasswordNotMatchException exception) {
                model.addAttribute("error", exception.getMessage());
            } catch (MessagingException | UnsupportedEncodingException exception) {
                model.addAttribute("error", "Nie udało nam się wysłać maila potwierdzającego rejestracje, skontaktuj się z administratorem");
            }
        }
        return "register";
    }

    @GetMapping("/activate_account")
    public String processActivateUserAccount(@RequestParam(name = "uuid", defaultValue = "error") String uuid, Model model) {
        try {
            userService.processActivateUserAccount(uuid);
        } catch (InvalidTokenException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user_activate_account";
    }

}
