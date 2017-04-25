package com.example.product.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by sajacaros on 2017-04-24.
 */
public class ProductDto {
    @Getter
    @NoArgsConstructor
    @ToString
    public static class New {
        private String name;
        private Integer price;

        public New(String name, Integer price) {
            this.name = name;
            this.price = price;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Response {
        private Long id;
        @JsonUnwrapped
        private New model;

        public Response(Long id, String name, Integer price) {
            this.id = id;
            model = new New(name, price);
        }

        public String name() {
            return model.getName();
        }

        public Integer price() {
            return model.getPrice();
        }
    }
}
