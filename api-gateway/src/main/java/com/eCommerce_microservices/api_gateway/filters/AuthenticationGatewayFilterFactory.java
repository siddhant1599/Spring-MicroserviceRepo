package com.eCommerce_microservices.api_gateway.filters;

import com.eCommerce_microservices.api_gateway.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(!config.isEnabled) return chain.filter(exchange);
            String authorizationHeaders= exchange.getRequest().getHeaders().get("Authorization").getFirst();

            if(authorizationHeaders ==null){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeaders.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);

             exchange.getRequest()
                     .mutate()
                     .header("X-User-Id", userId.toString())
                     .build();

            return chain.filter(exchange);
        });
    }

    public static class Config{
        private boolean isEnabled;
    }
}
