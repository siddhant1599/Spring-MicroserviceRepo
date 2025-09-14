package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.OrderRequestDto;
import com.microservice.inventory_service.dto.OrderRequestItemDto;
import com.microservice.inventory_service.dto.ProductDto;
import com.microservice.inventory_service.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.microservice.inventory_service.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public List<ProductDto> getAllInventory(){
        log.info("Fetching all product inventories");
        List<Product> productEntities = productRepository.findAll();

        return productEntities.stream().map(product -> {
            return modelMapper.map(product, ProductDto.class);
        }).collect(Collectors.toList());
    }


    public ProductDto getProductById(Long id){

        log.info("Fetching product with id - " + id);

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("product does not exist with id - " + id));

        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {

        log.info("Reducing the stocks");
        Double totalPrice = 0.0;

        for(OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItemList()){
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exist by id - " + productId));

            if(quantity >= product.getStock()){
                product.setStock(product.getStock() - quantity);
            }
            else{
                throw new RuntimeException("Order request cannot be fulfilled for given quantity");
            }

            productRepository.save(product);

            totalPrice+= quantity*product.getPrice();
        }

        return totalPrice;
    }
}
