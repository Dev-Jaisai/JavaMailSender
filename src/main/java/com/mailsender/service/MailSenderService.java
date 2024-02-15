package com.mailsender.service;

import com.mailsender.entity.MailSenderEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class MailSenderService {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(MailSenderEntity mailSenderEntity) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(mailSenderEntity.getSubject());
        simpleMailMessage.setTo(mailSenderEntity.getTo());
        simpleMailMessage.setText(mailSenderEntity.getBody());
        javaMailSender.send(simpleMailMessage);
    }
}
