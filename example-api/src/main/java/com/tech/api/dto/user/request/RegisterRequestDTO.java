package com.tech.api.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class RegisterRequestDTO implements Serializable {

    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

}
