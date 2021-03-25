package catsmall.cat.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlMailSender implements MyMailSender {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(EmailMessage emailMessage){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
            messageHelper.setTo(emailMessage.getTo());
            messageHelper.setText(emailMessage.getMessage(), true);
            messageHelper.setSubject(emailMessage.getSubject());
            log.info("binding MimeMessage Success = " + emailMessage.getMessage());
        } catch (MessagingException e) {
            log.info("failed to binding MimeMessage = " + e.getMessage());
        }
        javaMailSender.send(mimeMessage);
    }
}
