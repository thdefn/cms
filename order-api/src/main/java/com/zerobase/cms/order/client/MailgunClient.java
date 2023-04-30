package com.zerobase.cms.order.client;

import com.zerobase.cms.order.client.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {
    @PostMapping("sandbox56b1ba9964fb4b409399c2c1bb6b3995.mailgun.org/messages")
    String sendEmail(@SpringQueryMap SendMailForm form);
}
