package com.tech.api.service.impl;

import com.tech.api.dto.user.request.LoginRequestDTO;
import com.tech.api.dto.user.response.LoginResponseDTO;
import com.tech.api.dto.user.response.RegisterResponseDTO;
import com.tech.api.dto.user.response.UserInfoResponseDTO;
import com.tech.api.exception.EmailAlreadyInUseException;
import com.tech.api.dto.user.request.RegisterRequestDTO;
import com.tech.api.entity.Role;
import com.tech.api.entity.RoleName;
import com.tech.api.entity.User;
import com.tech.api.exception.RoleNotFoundException;
import com.tech.api.repository.RoleRepository;
import com.tech.api.repository.UserRepository;
import com.tech.api.security.JwtTokenProvider;
import com.tech.api.security.UserPrincipal;
import com.tech.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponseDTO(tokenProvider.generateToken(authentication));
    }

    @Override
    public RegisterResponseDTO createUser(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyInUseException("Email Address already in use!");
        }
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(String.format("User role '%s' is not found.", RoleName.ROLE_USER)));
        saveUser(registerRequestDTO, userRole);
        return new RegisterResponseDTO("User has been created successfully.");
    }

    @Override
    public UserInfoResponseDTO getUserInfo(UserPrincipal currentUser) {
        UserInfoResponseDTO userInfoResponseDTO = new UserInfoResponseDTO();
        userInfoResponseDTO.setEmail(currentUser.getEmail());
        userInfoResponseDTO.setName(currentUser.getName());
        return userInfoResponseDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveUser(RegisterRequestDTO registerRequestDTO, Role userRole) {
        User user = new User(
                registerRequestDTO.getName(),
                registerRequestDTO.getEmail(),
                registerRequestDTO.getPhoneNumber(),
                passwordEncoder.encode(registerRequestDTO.getPassword())
        );
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

}
