package pl.saqie.producttracker.app.services.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
public class RegisterEmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailMessage(String to, String uuid, String applicationAdress) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("admin@producttracker.com", "Rejestracja");
        messageHelper.setTo(to);
        messageHelper.setSubject("ProductTracker");
        String link = createFinalLink(applicationAdress, uuid);
        messageHelper.setText(emailText(link), true);

        mailSender.send(message);
    }

    private String createFinalLink(String applicationAdress, String uuid) {
        return applicationAdress + "/activate_account?uuid=" + uuid;
    }


    @Override
    public String emailText(String link) {
        return "<!DOCTYPE html><head><meta charset=\"UTF-8\"></head><body><center><b>Product_Tracker - Rejestracja<b><br>" +
                "<center>Witaj, dziękujemy za rejestracje w naszym serwisie, prosimy o potwierdzenie adresu email klikając w link poniżej</center><br>" +
                "<center><b><p><a href=\"" + link + "\"> Potwierdz rejestrację </p></b></a></center><br>" +
                "<center><b style=\"color:red;\">Prosimy nie odpowiadać na tego maila.</b></center></body></html>";
    }
}
