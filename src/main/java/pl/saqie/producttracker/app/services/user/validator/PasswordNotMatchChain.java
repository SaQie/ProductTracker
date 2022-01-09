package pl.saqie.producttracker.app.services.user.validator;

import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.error.user.PasswordNotMatchException;
import pl.saqie.producttracker.app.services.user.dto.RegisterDto;

@Component
public class PasswordNotMatchChain implements UserValidatorChain {

    @Override
    public void chain(RegisterDto registerDto) throws PasswordNotMatchException {
        if (!registerDto.getPassword().equals(registerDto.getPasswordRepeat())) {
            throw new PasswordNotMatchException("Hasła nie są zgodne.");
        }
    }
}
