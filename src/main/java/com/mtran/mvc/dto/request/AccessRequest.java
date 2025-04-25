package com.mtran.mvc.dto.request;

import lombok.Data;

@Data
public class AccessRequest {
    private String email;
    private String password;
}
