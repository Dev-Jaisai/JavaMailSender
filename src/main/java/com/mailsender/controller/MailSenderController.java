package com.mailsender.controller;

import com.mailsender.entity.MailSenderEntity;
import com.mailsender.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailSenderController {
    @Autowired
    MailSenderService mailSenderService;

    @PostMapping("/send")
    public String mailSend(@RequestBody MailSenderEntity mailSenderEntity) {
        mailSenderService.sendEmail(mailSenderEntity);
        return "Successfully sent email to " + mailSenderEntity.getTo();
    }
}
