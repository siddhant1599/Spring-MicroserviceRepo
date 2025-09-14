package com.microservice.order_service.service;

import com.microservice.order_service.clients.InventoryClient;
import com.microservice.order_service.dto.OrderRequestDto;
import com.microservice.order_service.entity.OrderItem;
import com.microservice.order_service.entity.Orders;
import com.microservice.order_service.enums.OrderStatus;
import com.microservice.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;


    public List<OrderRequestDto> getAllOrders(){
        List<Orders> ordersList = ordersRepository.findAll();

        return ordersList.stream().map(orders -> modelMapper.map(orders, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id){
        Orders order = ordersRepository.findById(id).orElseThrow(() -> new RuntimeException("order does not exist by id -" + id));
        return modelMapper.map(order, OrderRequestDto.class);
    }
    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {

        Double totalPrice = inventoryClient.reduceStock(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);

        for(OrderItem orderItem : orders.getItemList()){
            orderItem.setOrders(orders);
        }

        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

        ordersRepository.save(orders);

        return modelMapper.map(orders, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Fallback occurred due to : {}", throwable.getMessage());

        return new OrderRequestDto();
    }
}
