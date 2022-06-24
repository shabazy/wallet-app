package com.tech.api.dto.transaction.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SendMoneyResponseDTO implements Serializable {

    @JsonProperty("source")
    private WalletTransactionResponseDTO source;

    @JsonProperty("target")
    private WalletTransactionResponseDTO target;

    public SendMoneyResponseDTO(WalletTransactionResponseDTO source, WalletTransactionResponseDTO target) {
        this.source = source;
        this.target = target;
    }

}
