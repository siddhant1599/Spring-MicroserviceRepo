package com.microservice.inventory_service.dto;

import com.microservice.inventory_service.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private List<OrderRequestItemDto> itemList;
}
