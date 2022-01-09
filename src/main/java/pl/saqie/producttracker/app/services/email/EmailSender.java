package pl.saqie.producttracker.app.services.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
public interface EmailSender {

    @Async
    void sendEmailMessage(String to, String uuid, String link) throws MessagingException, UnsupportedEncodingException;

    String emailText(String link);

}
