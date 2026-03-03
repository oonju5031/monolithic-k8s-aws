package com.example.monolithic.order.domain.dto;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private Long productId;
    private Integer qty;

}
