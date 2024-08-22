package com.cami.gallerycove.service.MailService;

import com.cami.gallerycove.domain.model.MailStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final String fromMail;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromMail) {
        this.mailSender = mailSender;
        this.fromMail = fromMail;
    }

    @Override
    public void sendEmail(String email, MailStructure mailStructure) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(mailStructure.getMessage());
        simpleMailMessage.setTo(email);

        mailSender.send(simpleMailMessage);
    }
}
