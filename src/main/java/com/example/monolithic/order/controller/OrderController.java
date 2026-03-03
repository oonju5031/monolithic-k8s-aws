package com.example.monolithic.order.controller;

import com.example.monolithic.order.domain.dto.OrderRequestDTO;
import com.example.monolithic.order.domain.dto.OrderResponseDTO;
import com.example.monolithic.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody OrderRequestDTO request) {
        log.info("OrderController create: {}", request);

        OrderResponseDTO response = orderService.orderCreate(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
