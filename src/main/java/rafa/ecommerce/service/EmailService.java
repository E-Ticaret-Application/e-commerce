package rafa.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import rafa.ecommerce.domain.EmailConfirmationToken;
import rafa.ecommerce.domain.User;
import rafa.ecommerce.repository.ConfirmationTokenRepository;
import rafa.ecommerce.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Value("${app.baseUrl}")
    private String baseUrl;

    @Transactional
    public void sendEmail(User user) throws UnsupportedEncodingException, MessagingException {

        String token = UUID.randomUUID().toString();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(token, user);
        confirmationTokenRepository.save(emailConfirmationToken);

        String toAddress = user.getEmail();
        String fromAddress = "rmzn.dmr.info@gmail.com";
        String senderName = "For Textile aş";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "For Textile aş.";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = baseUrl + "/api/v1/auth/confirm-account?token="
                + emailConfirmationToken.getConfirmationToken();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);

        javaMailSender.send(message);

    }

    public String confirmEmail(String confirmationToken) {
        EmailConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken).get();

        if (token != null) {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail()).get();
            user.setEmailVerified(true);
            userRepository.save(user);
            return "Email verified successfully!";
        }

        return "Error: Couldn't verify email";

    }
}
