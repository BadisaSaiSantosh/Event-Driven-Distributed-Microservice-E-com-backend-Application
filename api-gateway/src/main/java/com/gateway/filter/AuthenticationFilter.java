package com.gateway.filter;

import com.gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check if the Authorization header exists
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7); // Remove "Bearer " prefix
                }

                try {
                    // Validate token signature
                    jwtUtil.validateToken(authHeader);

                    // Extract data from the token
                    Claims claims = jwtUtil.extractAllClaims(authHeader);
                    String userId = claims.getSubject(); 
                    String userRole = claims.get("role", String.class); 

                    // Mutate the request to append our trusted internal headers
                    exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-User-Role", userRole)
                            .build();

                } catch (Exception e) {
                    System.out.println("Invalid access...!");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            
            // Pass the mutated request down the chain to the destination microservice
            return chain.filter(exchange);
        });
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }
}