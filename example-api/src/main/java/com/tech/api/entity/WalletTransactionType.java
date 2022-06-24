package com.tech.api.entity;

public enum WalletTransactionType {

    TOP_UP("Top Up"),
    PAYMENT("Payment");

    private String name;

    WalletTransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static WalletTransactionType getByName(String name) {
        return valueOf(name);
    }

}
