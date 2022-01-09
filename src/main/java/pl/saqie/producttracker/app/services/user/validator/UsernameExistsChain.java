package pl.saqie.producttracker.app.services.user.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.saqie.producttracker.app.error.user.UserAlreadyExistsException;
import pl.saqie.producttracker.app.repository.UserRepository;
import pl.saqie.producttracker.app.services.user.dto.RegisterDto;

@Component
@AllArgsConstructor
public class UsernameExistsChain implements UserValidatorChain {

    private final UserRepository userRepository;

    @Override
    public void chain(RegisterDto registerDto) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UserAlreadyExistsException("Taka nazwa użytkownika już istnieje");
        }
    }
}
