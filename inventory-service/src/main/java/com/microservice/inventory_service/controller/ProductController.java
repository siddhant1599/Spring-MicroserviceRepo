package com.microservice.inventory_service.controller;

import com.microservice.inventory_service.clients.OrdersClient;
import com.microservice.inventory_service.dto.OrderRequestDto;
import com.microservice.inventory_service.dto.ProductDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.microservice.inventory_service.service.ProductService;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/inventory/products")
public class ProductController {


    private final ProductService productService;
    private final RestClient restClient;
    private final DiscoveryClient discoveryClient;

    private final OrdersClient ordersClient;

    @GetMapping("/testEndpointInventoryService")
    public String testEndpointInventoryService(HttpServletRequest request){
        log.info(request.getHeader("*************"+ "X-Custom-Header") + "*************");

//        ServiceInstance orderService = discoveryClient.getInstances("ORDER-SERVICE").getFirst();
//        URI BASE_URI = orderService.getUri();
//        return restClient.get()
//                .uri(BASE_URI+ "/orders/core/testEndpointOrderService")
//                .retrieve()
//                .body(String.class);

        return ordersClient.testEndpointOrderService();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory(){
        List<ProductDto> productDtos = productService.getAllInventory();

        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getInventoryById(@PathVariable Long id){
        ProductDto product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto){
        Double totalPrice = productService.reduceStocks(orderRequestDto);

        return ResponseEntity.ok(totalPrice);
    }
}
