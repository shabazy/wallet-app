package com.tech.api.util;

import com.tech.api.dto.ApiException;
import com.tech.api.dto.ApiResponse;
import com.tech.api.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ResponseBody
    @ExceptionHandler({ApiException.class})
    @ResponseStatus(HttpStatus.OK)
    public final ApiResponse<ApiException> handleAllApiExceptions(Exception ex, WebRequest request) {
        ApiException apiException = (ApiException) ex;
        return new ApiResponse<>(apiException);
    }

    @ResponseBody
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ApiResponse<ApiException> handleAuthenticationExceptions(AuthenticationException ex) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        log.error("Bad credentials exception caught", ex);
        return new ApiResponse<>(apiException);
    }

    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ApiResponse<ApiException> handleAllExceptions(Exception ex) {
        ApiException apiException = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        log.error("Runtime exception caught", ex);
        return new ApiResponse<>(apiException);
    }

    @ResponseBody
    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ApiResponse<ApiException> handleValidationExceptions(ValidationException ex) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        log.error("Validation exception caught", ex);
        return new ApiResponse<>(apiException);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<ApiException> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError error = (FieldError) ex.getBindingResult().getFieldErrors().iterator().next();
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST.value(), error.getDefaultMessage());
        log.warn(error.getDefaultMessage());
        return new ApiResponse<>(apiException);
    }
}