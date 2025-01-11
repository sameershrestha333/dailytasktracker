package com.dailytasktracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {
    @Autowired
    TaskEmailScheduler emailService;

    @Test
    void testSendEmail() {
        emailService.sendEmail(
            "sameershrestha333@gmail.com",
            "Test Subject", 
            "<h1>Test Body</h1>"
        );
    }
}
