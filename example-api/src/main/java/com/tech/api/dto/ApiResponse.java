package com.tech.api.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private T data;

    private ApiResult result;

    public ApiResponse(T data) {
        this.data = data;
        this.result = new ApiResult(0, "success");
    }

    public ApiResponse(ApiException exception) {
        this.result = exception.getApiResult();
    }

}