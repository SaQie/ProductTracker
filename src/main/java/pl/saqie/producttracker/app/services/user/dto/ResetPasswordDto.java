package pl.saqie.producttracker.app.services.user.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordDto {

    @Size(min = 5, message = "Nowe hasło musi posiadać co najmniej 5 znaków.")
    private String newPassword;
    private String newPasswordRepeat;
    private String resetUUID;
}
