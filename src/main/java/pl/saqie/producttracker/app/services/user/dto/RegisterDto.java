package pl.saqie.producttracker.app.services.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDto {

    @Size(min = 3, max = 10, message = "Nazwa użytkownika musi zawierać od 3 do 10 znaków")
    private String username;
    @Email(message = "Podaj poprawny adres E-Mail")
    @NotEmpty(message = "Adres E-mail nie może pozostać pusty")
    private String email;
    @Size(min = 5, message = "Hasło musi zawierać co najmniej 5 znaków")
    private String password;
    private String passwordRepeat;

}
