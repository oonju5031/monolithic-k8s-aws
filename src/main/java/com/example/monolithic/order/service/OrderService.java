package com.example.monolithic.order.service;

import com.example.monolithic.order.domain.dto.OrderRequestDTO;
import com.example.monolithic.order.domain.dto.OrderResponseDTO;
import com.example.monolithic.order.domain.entity.OrderEntity;
import com.example.monolithic.order.domain.entity.OrderStatus;
import com.example.monolithic.order.repository.OrderRepository;
import com.example.monolithic.product.domain.entity.ProductEntity;
import com.example.monolithic.product.repository.ProductRepository;
import com.example.monolithic.user.domain.entity.UserEntity;
import com.example.monolithic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderResponseDTO orderCreate(OrderRequestDTO request) {
        log.info(">>> OrderService orderCreate");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(">>> auth getName: {}", auth.getName());

        UserEntity user = userRepository.findById(auth.getName())
                .orElseThrow(() -> new RuntimeException("User Not Found."));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not Found."));

        // 재고 관리
        Integer qty = request.getQty();

        if (product.getStockQty() >= qty) {
            product.updateStockQty(qty);
        } else {
            throw new RuntimeException("재고가 부족합니다.");
        }

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .product(product)
                .qty(request.getQty())
                .build();

        return OrderResponseDTO.fromEntity(orderRepository.save(order));

    }
}
