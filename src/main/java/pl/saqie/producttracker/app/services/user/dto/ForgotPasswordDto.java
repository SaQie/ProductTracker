package pl.saqie.producttracker.app.services.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForgotPasswordDto {

    @Email(message = "Wporwadz poprawny adres E-Mail.")
    private String email;

}
