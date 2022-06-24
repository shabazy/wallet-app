package com.tech.api.unit.service;

import com.tech.api.dto.user.request.RegisterRequestDTO;
import com.tech.api.dto.user.response.RegisterResponseDTO;
import com.tech.api.entity.Role;
import com.tech.api.entity.RoleName;
import com.tech.api.entity.User;
import com.tech.api.repository.RoleRepository;
import com.tech.api.repository.UserRepository;
import com.tech.api.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testCreateUser() {
        String email = "gokhan@ensep.com";
        String password = "pass";
        String name = "Gokhan Ensep";
        String phoneNumber = "+90328333232";
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail(email);
        registerRequestDTO.setPassword(password);
        registerRequestDTO.setPhoneNumber(phoneNumber);
        registerRequestDTO.setName(name);
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        User user = new User(name, email, phoneNumber, password);
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(java.util.Optional.of(role));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("32138dkshajk");
        RegisterResponseDTO registerResponseDTO = userService.createUser(registerRequestDTO);
        Assertions.assertEquals("User has been created successfully.", registerResponseDTO.getMessage());
    }

}
