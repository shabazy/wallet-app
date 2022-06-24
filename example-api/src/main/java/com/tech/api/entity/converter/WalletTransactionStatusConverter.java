package com.tech.api.entity.converter;

import com.tech.api.entity.WalletTransactionStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class WalletTransactionStatusConverter implements AttributeConverter<WalletTransactionStatus, String> {

    public WalletTransactionStatusConverter() {
    }

    public String convertToDatabaseColumn(WalletTransactionStatus walletTransactionStatus) {
        return walletTransactionStatus.toString();
    }

    public WalletTransactionStatus convertToEntityAttribute(String name) {
        return WalletTransactionStatus.getByName(name);
    }
}
