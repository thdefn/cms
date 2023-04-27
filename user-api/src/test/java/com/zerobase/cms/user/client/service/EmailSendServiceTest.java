package com.zerobase.cms.user.client.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EmailSendServiceTest {
    @Autowired
    private MailgunClient mailgunClient;

    @Test
    void SuccessEmailTest() {
        //given
        SendMailForm form = SendMailForm.builder()
                .from("thdefn@gmail.com")
                .to("thdefn@gmail.com")
                .subject("test email from zero base")
                .text("my text")
                .build();
        //when
        String result = mailgunClient.sendEmail(form);
        //then
        assertNotNull(result);
    }
}
