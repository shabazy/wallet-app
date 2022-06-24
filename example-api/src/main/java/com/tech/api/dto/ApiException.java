package com.tech.api.dto;

import lombok.Data;

@Data
public class ApiException extends RuntimeException {

    private final int errorCode;
    private String errorMessage;

    public ApiException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApiResult getApiResult() {
        return new ApiResult(this.errorCode, this.errorMessage);
    }

}
