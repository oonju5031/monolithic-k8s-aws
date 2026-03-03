package com.example.monolithic.product.service;

import com.example.monolithic.product.domain.dto.ProductRequestDTO;
import com.example.monolithic.product.domain.dto.ProductResponseDTO;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductResponseDTO productCreate(ProductRequestDTO request) {
        log.info(">>> ProductService productCreate");

        // 로그인한 유저 데이터 호출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(">>> auth getName: {}", auth.getName());

        UserEntity user = userRepository.findById(auth.getName())
                .orElseThrow(() -> new RuntimeException("User Not Found."));

        ProductEntity product = request.toEntity(user);
        return ProductResponseDTO.fromEntity(productRepository.save(product));
    }
}
