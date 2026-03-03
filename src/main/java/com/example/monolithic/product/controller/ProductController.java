package com.example.monolithic.product.controller;

import com.example.monolithic.product.domain.dto.ProductRequestDTO;
import com.example.monolithic.product.domain.dto.ProductResponseDTO;
import com.example.monolithic.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> getMethodName(ProductRequestDTO request) {
        log.info(">>> ProductController create: {}", request);

        ProductResponseDTO response = productService.productCreate(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
