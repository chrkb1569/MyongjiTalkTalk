package com.example.demo.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {
    private String grantedType;
    private String originToken;
    private String refreshToken;
    private Long expirationTime;
}
