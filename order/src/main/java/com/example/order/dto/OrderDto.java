package com.example.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by sajacaros on 2017-04-26.
 */
public class OrderDto {
    @NoArgsConstructor
    @Getter
    @ToString
    public static class New {
        private String principalId;
        private Long productId;
        private Integer quantity;

        public New(String principalId, Long productId, Integer quantity) {
            this.principalId = principalId;
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
