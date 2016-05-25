package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String product;
    private Integer count;
    private BigDecimal price;
}
