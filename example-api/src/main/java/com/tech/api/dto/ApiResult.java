package com.tech.api.dto;

import lombok.Data;

@Data
public class ApiResult {

    private int code;
    private String message;

    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
