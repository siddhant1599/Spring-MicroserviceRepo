package com.microservice.order_service.controller;

import com.microservice.order_service.clients.InventoryClient;
import com.microservice.order_service.dto.OrderRequestDto;
import com.microservice.order_service.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/core")
@RequiredArgsConstructor
@Slf4j
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("/testEndpointOrderService")
    public String testEndpointOrderService(@RequestHeader("X-User-Id") Long userId){

        return "Hello from the order Service, user id is -" + userId;
    }

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(){
        List<OrderRequestDto> orderRequestDtos = ordersService.getAllOrders();
        return ResponseEntity.ok(orderRequestDtos);
    }


    @GetMapping("{id}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long id){
        OrderRequestDto orderRequestDto = ordersService.getOrderById(id);
        return ResponseEntity.ok(orderRequestDto);
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto){
       return ResponseEntity.ok(ordersService.createOrder(orderRequestDto));
    }
}
