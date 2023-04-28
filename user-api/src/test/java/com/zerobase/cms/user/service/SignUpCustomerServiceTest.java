package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SignUpCustomerServiceTest {
    @Autowired
    private SignUpCustomerService signUpCustomerService;


    @Test
    void SuccessSignupTest() {
        //given
        SignUpForm form = SignUpForm.builder()
                .birth(LocalDate.now())
                .email("abcde@gmail.com")
                .password("123456")
                .phone("01023322233")
                .name("하이")
                .build();
        //when
        Customer customer = signUpCustomerService.signup(form);
        //then
        assertEquals(form.getEmail().toLowerCase(Locale.ROOT), customer.getEmail());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getId());
    }

}