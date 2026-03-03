package com.example.monolithic.product.domain.dto;

import com.example.monolithic.product.domain.entity.ProductEntity;
import com.example.monolithic.user.domain.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private String name;
    private Integer price;
    private Integer stockQty;

    public ProductEntity toEntity(UserEntity user) {
        return ProductEntity.builder()
                .name(this.name)
                .price(this.price)
                .stockQty(this.stockQty)
                .user(user)
                .build();
    }

}
