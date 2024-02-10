package com.hyperion.yellowcarbff.controllers;

import com.hyperion.yellowcarbff.model.responses.BasicResponse;
import com.hyperion.yellowcarbff.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private MailService mailService;

    @PostMapping("/test")
    public BasicResponse sendTestMail(@RequestParam(name = "email") String email) {
        return mailService.testMailService(email);
    }

}
