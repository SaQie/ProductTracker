package pl.saqie.producttracker.app.services.user.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangePasswordDto {

    @NotEmpty(message = "Wprowadz aktualne hasło.")
    private String currentPassword;
    @Size(min = 5, message = "Nowe hasło musi posiadać co najmniej 5 znaków")
    private String newPassword;

}
