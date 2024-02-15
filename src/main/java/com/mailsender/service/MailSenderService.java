package com.mailsender.service;

import com.mailsender.entity.MailSenderEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j

public class MailSenderService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private OTPService otpService; // Assuming you have an OTPService bean


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

    // Function to call OTPService for generating OTP
    public String generateOTP() {
        String userId = UUID.randomUUID().toString();
        log.info("otp for id {} is created",userId);
        return otpService.generateOTP(userId);
    }

    // Function to send OTP to a user's email
    public void sendOTP(String email) {
        String otp = generateOTP();

        // Send OTP to the user's email
        MailSenderEntity mailSenderEntity = new MailSenderEntity();
        mailSenderEntity.setTo(email);
        mailSenderEntity.setSubject("Your OTP for verification");
        mailSenderEntity.setBody("Your OTP is: " + otp);
        sendEmail(mailSenderEntity);
    }

}
