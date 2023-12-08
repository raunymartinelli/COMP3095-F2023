package ca.gbc.orderservice.controller;


import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.service.OrderServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeOrderFallBack")
    public String placeOrder(@RequestBody OrderRequest request) {
        orderService.placeOrder(request);
        return "Order placed successfully";
    }

    public String placeOrderFallBack(OrderRequest request, RuntimeException e) {
        log.error("Exception is: {}", e.getMessage());
        return "FAllBACK INVOKED: Order Placed Failed. Please try again later.";
    }
}

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name = "inventory", fallbackMethod = "placeOrderFallBack")
//    @TimeLimiter(name = "inventory")
//    @Retry(name="inventory")
//    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest request) {
//        orderService.placeOrder(request);
//        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(request));
//    }
//
//    public CompletableFuture<String> placeOrderFallBack(OrderRequest request, RuntimeException e){
//        log.error("Exception is: {}", e.getMessage());
//        return CompletableFuture.supplyAsync(() ->"FAllBACK INVOKED: Order Placed Failed. Please try again later.");
//    }


