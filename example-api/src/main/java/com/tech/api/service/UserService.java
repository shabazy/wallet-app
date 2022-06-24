package com.tech.api.service;

import com.tech.api.dto.user.request.LoginRequestDTO;
import com.tech.api.dto.user.request.RegisterRequestDTO;
import com.tech.api.dto.user.response.LoginResponseDTO;
import com.tech.api.dto.user.response.RegisterResponseDTO;
import com.tech.api.dto.user.response.UserInfoResponseDTO;
import com.tech.api.entity.Role;
import com.tech.api.security.UserPrincipal;

public interface UserService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO);

    void saveUser(RegisterRequestDTO registerRequestDTO, Role role);

    UserInfoResponseDTO getUserInfo(UserPrincipal currentUser);

}
