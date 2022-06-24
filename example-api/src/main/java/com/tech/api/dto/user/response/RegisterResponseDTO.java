package com.tech.api.dto.user.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterResponseDTO implements Serializable {

    private String message;

    public RegisterResponseDTO(String message) {
        this.message = message;
    }

}
