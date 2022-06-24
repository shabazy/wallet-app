package com.tech.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "wallet", indexes = {
        @Index(name = "wallet_user_id_idx", columnList = "user_id")
})
public class Wallet extends BaseEntity {

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "wallet_status")
    private WalletStatus walletStatus = WalletStatus.ACTIVE;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
