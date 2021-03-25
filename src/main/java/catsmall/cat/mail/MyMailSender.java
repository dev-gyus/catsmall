package catsmall.cat.mail;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MyMailSender{
    void sendMail(EmailMessage emailMessage);
}
