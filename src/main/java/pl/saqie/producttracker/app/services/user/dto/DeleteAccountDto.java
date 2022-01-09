package pl.saqie.producttracker.app.services.user.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteAccountDto {

    @NotEmpty(message = "Wprowadz aktualne hasło.")
    private String currentPassword;
}
