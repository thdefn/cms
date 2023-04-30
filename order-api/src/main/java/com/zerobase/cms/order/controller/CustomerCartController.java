package com.zerobase.cms.order.controller;

import com.zerobase.cms.domain.config.JwtAuthenticationProvider;
import com.zerobase.cms.order.application.CartApplication;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/cart")
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                        @RequestBody AddProductCartForm form) {
        return ResponseEntity.ok(
                cartApplication.addCart(
                        jwtAuthenticationProvider.getUserVo(token).getId(), form));
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(
                cartApplication.getCart(
                        jwtAuthenticationProvider.getUserVo(token).getId()));
    }

    @PutMapping
    public ResponseEntity<Cart> updateCart(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                           @RequestBody Cart cart) {
        return ResponseEntity.ok(
                cartApplication.updateCart(
                        jwtAuthenticationProvider.getUserVo(token).getId(), cart));
    }

}
