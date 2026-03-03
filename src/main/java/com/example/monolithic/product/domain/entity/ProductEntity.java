package com.example.monolithic.product.domain.entity;

import com.example.monolithic.common.domain.BaseTimeEntity;
import com.example.monolithic.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monolithic_product")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer price;
    private Integer stockQty;

    // 상품을 등록한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    public void updateStockQty(int stockQty) {
        this.stockQty -= stockQty;
    }
}
