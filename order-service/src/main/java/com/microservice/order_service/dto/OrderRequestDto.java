package com.microservice.order_service.dto;

import com.microservice.order_service.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    private Long id;
    private OrderStatus orderStatus;
    private Double price;
    private List<OrderRequestItemDto> itemList;
}
