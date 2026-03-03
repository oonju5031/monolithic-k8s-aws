package com.example.monolithic.user.domain.dto;

import com.example.monolithic.user.domain.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String email;
    private String password;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
