package com.tech.api.dto.transaction.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.api.entity.WalletTransaction;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WalletTransactionResponseDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("description")
    private String description;

    @JsonProperty("transaction_status")
    private String walletTransactionStatus;

    @JsonProperty("transaction_type")
    private String transactionType;

    public WalletTransactionResponseDTO(WalletTransaction walletTransaction) {
        this.id = walletTransaction.getId();
        this.reference = walletTransaction.getReference();
        this.date = walletTransaction.getDate();
        this.amount = walletTransaction.getAmount();
        this.description = walletTransaction.getDescription();
        this.walletTransactionStatus = walletTransaction.getWalletTransactionStatus().getName();
        this.transactionType = walletTransaction.getTransactionType().getName();
    }

}
