package com.mtran.mvc.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDateTime dob;
    private String address;
    private String imageUrl;
    private String userKeycloakId;
}
