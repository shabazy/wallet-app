package com.tech.api.dto.wallet.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.api.entity.WalletStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateWalletRequestDTO implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private WalletStatus walletStatus = WalletStatus.ACTIVE;

}
