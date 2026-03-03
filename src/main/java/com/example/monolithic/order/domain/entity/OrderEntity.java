package com.example.monolithic.order.domain.entity;

import com.example.monolithic.common.domain.BaseTimeEntity;
import com.example.monolithic.product.domain.entity.ProductEntity;
import com.example.monolithic.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monolithic_order")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private ProductEntity product;

    private Integer qty;

    @Enumerated(EnumType.STRING)  // Enum 값을 DB에 저장하기 위해 자료형을 결정
    @Builder.Default  // 기본값(OrderStatus.ORDERED)이 무시되는 상황을 방지
    private OrderStatus orderStatus = OrderStatus.ORDERED;
}
