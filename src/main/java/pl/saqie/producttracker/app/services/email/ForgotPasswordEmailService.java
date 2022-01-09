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
public class ForgotPasswordEmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailMessage(String to, String uuid, String applicationAdress) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("admin@producttracker.com", "Przypomnienie hasła");
        messageHelper.setTo(to);
        messageHelper.setSubject("ProductTracker");
        String link = createFinalLink(applicationAdress, uuid);
        messageHelper.setText(emailText(link), true);

        mailSender.send(message);
    }

    private String createFinalLink(String applicationAdress, String uuid) {
        return applicationAdress + "/password_reset?uuid=" + uuid;
    }

    @Override
    public String emailText(String link) {
        return "<!DOCTYPE html><head><meta charset=\"UTF-8\"></head><body><center><b>Product_Tracker - Zapomniałem hasła<b><br>" +
                "<center>Witaj, wysłałeś prośbę o zresetowanie hasła do konta, kliknij w link poniżej i postępuj zgodnie z instrukcjami na ekranie</center><br>" +
                "<center><b><p><a href=\"" + link + "\"> Zresetuj hasło </p></b></a></center><br>" +
                "<center><b style=\"color:red;\">Prosimy nie odpowiadać na tego maila.</b></center></body></html>";
    }
}
