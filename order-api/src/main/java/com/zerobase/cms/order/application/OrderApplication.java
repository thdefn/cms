package com.zerobase.cms.order.application;

import com.zerobase.cms.order.client.MailgunClient;
import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.mailgun.SendMailForm;
import com.zerobase.cms.order.client.user.ChangeBalanceForm;
import com.zerobase.cms.order.client.user.CustomerDto;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import com.zerobase.cms.order.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class OrderApplication {

    private final CartApplication cartApplication;
    private final ProductItemService productItemService;
    private final UserClient userClient;

    private final MailgunClient mailgunClient;

    @Transactional
    public void order(String token, Cart cart) {
        Cart orderCart = cartApplication.refreshCart(cart);

        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART);
        }

        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();
        int totalPrice = getTotalPrice(cart);
        if (customerDto.getBalance() < totalPrice) {
            throw new CustomException(ErrorCode.ORDER_FAIL_NO_MONEY);
        }

        // 롤백 계획에 대해서 생각해야 함
        userClient.changeBalance(token,
                ChangeBalanceForm.builder()
                        .from("USER")
                        .message("Order")
                        .money(-totalPrice)
                        .build());

        String mailBody = modifyInventoryAndMakeMailBody(orderCart.getProducts(), totalPrice);
        sendOrderHistoryMail(customerDto.getEmail(), mailBody);
    }

    private Integer getTotalPrice(Cart cart) {
        return cart.getProducts().stream().flatMapToInt(
                        product -> product.getItems().stream().flatMapToInt(
                                productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
                .sum();
    }

    private String modifyInventoryAndMakeMailBody(List<Cart.Product> products, int totalPrice) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("안녕하세요, 고객님. ")
                .append(LocalDateTime.now() + " 에 주문하신 내역에 대해 안내드립니다. \n");

        for (Cart.Product product : products) {
            stringBuilder.append("---" + product.getName() + "---\n");
            for (Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount() - cartItem.getCount());
                stringBuilder.append(cartItem.getName() + " - " + cartItem.getPrice() * cartItem.getCount() + "\n");
            }
        }

        return stringBuilder.append("\n\n총 결제 금액 : " + totalPrice).toString();
    }

    private void sendOrderHistoryMail(String email, String body) {
        mailgunClient.sendEmail(SendMailForm.builder()
                .to(email)
                .from("ododieod@gmail.com")
                .subject("안녕하세요, 고객님. 주문 내역 안내드립니다. ")
                .text(body)
                .build());
    }

    // 결제를 위해 필요한 것
    // 1. 물건들이 전부 주문 가능한 상태인지 확인
    // 2. 가격 변동이 있었는지에 대해 확인
    // 3. 고객의 돈이 충분한지
    // 4. 결제 & 상품의 재고 관리
}
