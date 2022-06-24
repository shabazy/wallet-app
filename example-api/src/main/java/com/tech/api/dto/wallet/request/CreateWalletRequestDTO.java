package com.tech.api.dto.wallet.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateWalletRequestDTO implements Serializable {

    @JsonProperty("name")
    private String name;

}
