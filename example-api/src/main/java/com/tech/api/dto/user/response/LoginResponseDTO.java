package com.tech.api.dto.user.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponseDTO implements Serializable {

    public LoginResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    private String tokenType = "Bearer";

}
