package com.tech.api.dto.wallet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.api.entity.Wallet;
import com.tech.api.entity.WalletStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WalletResponseDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("status")
    private WalletStatus walletStatus;

    public WalletResponseDTO(Wallet wallet) {
        this.id = wallet.getId();
        this.name = wallet.getName();
        this.balance = wallet.getBalance();
        this.serialNumber = wallet.getSerialNumber();
        this.walletStatus = wallet.getWalletStatus();
    }

}
