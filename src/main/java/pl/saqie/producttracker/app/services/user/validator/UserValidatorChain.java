package pl.saqie.producttracker.app.services.user.validator;

import pl.saqie.producttracker.app.error.user.PasswordNotMatchException;
import pl.saqie.producttracker.app.error.user.UserAlreadyExistsException;
import pl.saqie.producttracker.app.services.user.dto.RegisterDto;

public interface UserValidatorChain {
    void chain(RegisterDto registerDto) throws UserAlreadyExistsException, PasswordNotMatchException;
}
