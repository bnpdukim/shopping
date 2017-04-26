package com.example.order.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by sajacaros on 2017-04-26.
 */
@Entity
@Table(name = "product_order")
@NoArgsConstructor
@ToString
@Getter
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String principalId;
    private Long productId;
    private Integer quantity;

    public Order(String principalId, Long productId, Integer quantity) {
        this.principalId = principalId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
