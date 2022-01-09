package pl.saqie.producttracker.app.services.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.saqie.producttracker.app.config.AppProperties;
import pl.saqie.producttracker.app.config.PasswordEncoder;
import pl.saqie.producttracker.app.domain.User;
import pl.saqie.producttracker.app.error.user.EmailNotExistsException;
import pl.saqie.producttracker.app.error.user.InvalidTokenException;
import pl.saqie.producttracker.app.error.user.PasswordNotMatchException;
import pl.saqie.producttracker.app.error.user.UserAlreadyExistsException;
import pl.saqie.producttracker.app.repository.UserRepository;
import pl.saqie.producttracker.app.services.email.ForgotPasswordEmailService;
import pl.saqie.producttracker.app.services.email.RegisterEmailService;
import pl.saqie.producttracker.app.services.mapper.user.UserMapper;
import pl.saqie.producttracker.app.services.user.dto.*;
import pl.saqie.producttracker.app.services.user.validator.UserValidatorChain;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final List<UserValidatorChain> userValidators;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final RegisterEmailService registerEmailService;
    private final ForgotPasswordEmailService forgotPasswordEmailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono takiego użytkownika"));
    }

    public void processSaveNewUser(RegisterDto registerDto) throws UserAlreadyExistsException, PasswordNotMatchException, MessagingException, UnsupportedEncodingException {
        startSaveProcess(registerDto);
    }

    private void startSaveProcess(RegisterDto registerDto) throws PasswordNotMatchException, UserAlreadyExistsException, MessagingException, UnsupportedEncodingException {
        validateUser(registerDto);
        User userToSave = getUserFromRegisterDto(registerDto);
        String uuid = generateRandomUUID();
        setUserFields(userToSave, uuid);
        sendEmailToUser(userToSave.getEmail(), uuid, appProperties.getApplicationAdress());
        saveNewUser(userToSave);
    }

    private void sendEmailToUser(String email, String uuid, String link) throws MessagingException, UnsupportedEncodingException {
        registerEmailService.sendEmailMessage(email, uuid, link);
    }

    private void saveNewUser(User userToSave) {
        repository.save(userToSave);
    }

    private void setUserFields(User userToSave, String uuid) {
        userToSave.setPassword(passwordEncoder.encoder().encode(userToSave.getPassword()));
        userToSave.setCreatedDate(LocalDate.now());
        userToSave.setNumberOfProducts(0);
        userToSave.setAccountEnabled(false);
        userToSave.setActivationTokenExpiredDate(LocalDateTime.now().plusMinutes(appProperties.getActivationTokenExpiredInMinutes()));
        userToSave.setActivationToken(uuid);
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private User getUserFromRegisterDto(RegisterDto registerDto) {
        return UserMapper.fromRegisterDtoToEntity(registerDto);
    }

    private void validateUser(RegisterDto registerDto) throws PasswordNotMatchException, UserAlreadyExistsException {
        for (UserValidatorChain userValidator : userValidators) {
            userValidator.chain(registerDto);
        }
    }

    public void processDeleteUserAccount(DeleteAccountDto deleteDto, User user) throws PasswordNotMatchException {
        checkPasswordIsCorrect(deleteDto.getCurrentPassword(), user);
        deleteUserAccount(user);
    }

    private void deleteUserAccount(User user) {
        repository.deleteById(user.getId());
    }

    public void processChangeUserPassword(ChangePasswordDto changePasswordDto, User user) throws PasswordNotMatchException {
        checkPasswordIsCorrect(changePasswordDto.getCurrentPassword(), user);
        updateUserPassword(user, changePasswordDto);
    }

    private void updateUserPassword(User user, ChangePasswordDto changePasswordDto) {
        user.setPassword(passwordEncoder.encoder().encode(changePasswordDto.getNewPassword()));
        repository.save(user);
    }

    private void checkPasswordIsCorrect(String password, User user) throws PasswordNotMatchException {
        if (!passwordEncoder.encoder().matches(password, user.getPassword())) {
            throw new PasswordNotMatchException("Niepoprawne hasło.");
        }
    }

    public void processActivateUserAccount(String uuid) throws InvalidTokenException {
        User user = checkUUID(uuid);
        updateUserAccountEnabled(user);
    }

    private void updateUserAccountEnabled(User user) {
        user.setAccountEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenExpiredDate(null);
        repository.save(user);
    }

    private User checkUUID(String uuid) throws InvalidTokenException {
        if (uuid.equals("error")) {
            throw new InvalidTokenException("Wprowadzony token aktywacyjny jest nieprawidłowy.");
        }
        User user = repository.findByActivationToken(uuid).orElseThrow(() -> new InvalidTokenException("Wprowadzony token aktywacyjny jest nieprawidłowy"));
        checkUUIDExpiredDate(user.getActivationTokenExpiredDate());
        return user;
    }

    private void checkUUIDExpiredDate(LocalDateTime activationTokenExpiredDate) throws InvalidTokenException {
        if (LocalDateTime.now().isAfter(activationTokenExpiredDate)) {
            throw new InvalidTokenException("Ważność tokenu aktywacyjnego wygasła.");
        }
    }

    public void processForgotPassword(ForgotPasswordDto forgotPasswordDto) throws EmailNotExistsException, MessagingException, UnsupportedEncodingException {
        User user = checkEmail(forgotPasswordDto.getEmail());
        String uuid = generateRandomUUID();
        forgotPasswordEmailService.sendEmailMessage(forgotPasswordDto.getEmail(), uuid, appProperties.getApplicationAdress());
        updateUserAccountForgotPasswordToken(user, uuid);
    }

    private void updateUserAccountForgotPasswordToken(User user, String uuid) {
        user.setForgotPasswordToken(uuid);
        repository.save(user);
    }

    private User checkEmail(String email) throws EmailNotExistsException {
        return repository.findByEmail(email).orElseThrow(() -> new EmailNotExistsException("Wskazany adres " + email + " nie istnieje."));
    }

    public ResetPasswordDto processCheckForgotPasswordToken(String uuid) throws InvalidTokenException {
        User user = repository.findByForgotPasswordToken(uuid).orElseThrow(() -> new InvalidTokenException("Token jest nieprawidłowy."));
        return UserMapper.fromEntityToResetPasswordDto(user);
    }

    public void processResetUserPassword(ResetPasswordDto resetPasswordDto) throws InvalidTokenException, PasswordNotMatchException {
        checkPasswordRepeat(resetPasswordDto);
        updateUserNewPassword(resetPasswordDto);
    }

    private void updateUserNewPassword(ResetPasswordDto resetPasswordDto) throws InvalidTokenException {
        User user = repository.findByForgotPasswordToken(resetPasswordDto.getResetUUID()).orElseThrow(() -> new InvalidTokenException("Wystąpił błąd, token jest nieprawidłowy."));
        user.setPassword(passwordEncoder.encoder().encode(resetPasswordDto.getNewPassword()));
        user.setForgotPasswordToken(null);
        repository.save(user);
    }

    private void checkPasswordRepeat(ResetPasswordDto resetPasswordDto) throws PasswordNotMatchException {
        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getNewPasswordRepeat())) {
            throw new PasswordNotMatchException("Musisz wprowadzić takie same hasła aby je zresetować.");
        }
    }

}
