package pl.saqie.producttracker.app.services.mapper.user;

import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.services.user.dto.RegisterDto;
import pl.saqie.producttracker.app.services.user.dto.ResetPasswordDto;

public class UserMapper {

    public static User fromRegisterDtoToEntity(RegisterDto registerDto) {
        return User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword()).build();
    }

    public static ResetPasswordDto fromEntityToResetPasswordDto(User user) {
        return ResetPasswordDto.builder()
                .resetUUID(user.getForgotPasswordToken()).build();
    }
}
