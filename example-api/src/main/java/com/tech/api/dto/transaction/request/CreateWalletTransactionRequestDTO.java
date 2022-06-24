package com.tech.api.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.api.entity.WalletTransactionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreateWalletTransactionRequestDTO implements Serializable {

    public CreateWalletTransactionRequestDTO() {

    }

    @JsonProperty("transaction_type")
    private WalletTransactionType transactionType;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date")
    private Date date = new Date();

    public CreateWalletTransactionRequestDTO(WalletTransactionType transactionType, BigDecimal amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }

}
