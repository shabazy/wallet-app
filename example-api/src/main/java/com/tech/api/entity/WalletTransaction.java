package com.tech.api.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "wallet_transaction")
public class WalletTransaction extends BaseEntity {

    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_status", nullable = false)
    private WalletTransactionStatus walletTransactionStatus = WalletTransactionStatus.SUCCEEDED;

    @Column(name = "transaction_type", nullable = false)
    private WalletTransactionType transactionType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

}
