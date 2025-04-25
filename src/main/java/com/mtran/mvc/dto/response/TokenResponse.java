package com.mtran.mvc.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {
    private final String accessToken;
    private final String refreshToken;
}
