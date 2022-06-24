package com.tech.api.controller;

import com.tech.api.dto.*;
import com.tech.api.dto.user.request.LoginRequestDTO;
import com.tech.api.dto.user.response.LoginResponseDTO;
import com.tech.api.dto.user.request.RegisterRequestDTO;
import com.tech.api.dto.user.response.RegisterResponseDTO;
import com.tech.api.dto.user.response.UserInfoResponseDTO;
import com.tech.api.security.CurrentUser;
import com.tech.api.security.UserPrincipal;
import com.tech.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin("*")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return new ApiResponse<>(userService.login(loginRequestDTO));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return new ApiResponse<>(userService.createUser(registerRequestDTO));
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<UserInfoResponseDTO> getUserInfo(@CurrentUser UserPrincipal currentUser) {
        return new ApiResponse<>(userService.getUserInfo(currentUser));
    }

}
