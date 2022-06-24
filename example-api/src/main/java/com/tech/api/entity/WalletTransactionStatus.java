package com.tech.api.entity;

import lombok.Data;

public enum WalletTransactionStatus {

    SUCCEEDED("Succeeded"),
    PENDING("Pending"),
    DECLINED("Declined");

    private String name;

    WalletTransactionStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static WalletTransactionStatus getByName(String name) {
        return valueOf(name);
    }


}
