package com.tech.api.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SendMoneyRequestDTO implements Serializable {

    @JsonProperty("target_wallet_serial_number")
    private String targetWalletSerialNumber;

    @JsonProperty("amount")
    private BigDecimal amount;

}
