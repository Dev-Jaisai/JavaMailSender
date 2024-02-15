package com.mailsender.controller;

import com.mailsender.service.MailSenderService;
import com.mailsender.service.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/otp")
public class OtpController {
    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    OTPService otpService;

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam String email) {
        mailSenderService.sendOTP(email);
        log.info(" Return a success message with the generated OTP");
        return "OTP sent successfully to " + email;
    }

    @PostMapping("/validate-otp")
    public String validateOTP(@RequestParam String userId, @RequestParam String otp) {
        boolean isValid = otpService.validateOTP(userId, otp);
        if (isValid) {
            log.info("OTP validation successful for user: {}", userId);
            return "OTP validation successful for user: " + userId;
        } else {
            log.warn("OTP validation failed for user: {}", userId);
            return "OTP validation failed for user: " + userId;
        }
    }
}