package com.tech.api.dto.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoResponseDTO implements Serializable {

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

}
