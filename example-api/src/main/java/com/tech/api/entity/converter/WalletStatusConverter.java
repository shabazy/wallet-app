package com.tech.api.entity.converter;

import com.tech.api.entity.WalletStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class WalletStatusConverter implements AttributeConverter<WalletStatus, String> {

    public WalletStatusConverter() {
    }

    public String convertToDatabaseColumn(WalletStatus walletStatus) {
        return walletStatus.toString();
    }

    public WalletStatus convertToEntityAttribute(String name) {
        return WalletStatus.getByName(name);
    }
}
