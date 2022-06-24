package com.tech.api.entity;

public enum WalletStatus {

    ACTIVE,
    PASSIVE;

    public static WalletStatus getByName(String name) {
        return valueOf(name);
    }

}
