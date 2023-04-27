package com.zerobase.cms.user.client.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final MailgunClient mailgunClient;

    public String sendMail(){
        SendMailForm form = SendMailForm.builder()
                .from("thdefn@gmail.com")
                .to("thdefn@gmail.com")
                .subject("test email from zero base")
                .text("my text")
                .build();

        return mailgunClient.sendEmail(form);
    }

}
