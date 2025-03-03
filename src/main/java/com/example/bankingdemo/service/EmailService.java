package com.example.bankingdemo.service;

import com.example.bankingdemo.dto.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    //    private static final Logger log = LoggerFactory.getLogger(Email.class);
    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(Email email) {
        if (email == null || email.getRecipient() == null || email.getSubject() == null || email.getMessage() == null) {
            throw new IllegalArgumentException("Email details cannot be null");
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(email.getRecipient());
            mailMessage.setSubject(email.getSubject());
            mailMessage.setText(email.getMessage());
            javaMailSender.send(mailMessage);
            log.info("Email sent successfully");

        } catch (MailException e) {
//            log.error("failed to send mail " + e);
            log.error("failed to send mail " + e.getMessage());
        }
    }

}
