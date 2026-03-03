package com.example.monolithic.common.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass  // 테이블을 생성하지 않으며, 자식 엔티티에게 매핑 정보만을 제공
@Getter
public class BaseTimeEntity {

    @CreationTimestamp
    private LocalDateTime createAt;

    @CreationTimestamp
    private LocalDateTime updateAt;

    @CreationTimestamp
    private LocalDateTime deleteAt;
}
