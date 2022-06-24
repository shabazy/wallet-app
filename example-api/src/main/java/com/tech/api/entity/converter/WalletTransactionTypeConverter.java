package com.tech.api.entity.converter;

import com.tech.api.entity.WalletTransactionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class WalletTransactionTypeConverter implements AttributeConverter<WalletTransactionType, String> {

    public WalletTransactionTypeConverter() {
    }

    public String convertToDatabaseColumn(WalletTransactionType transactionType) {
        return transactionType.toString();
    }

    public WalletTransactionType convertToEntityAttribute(String name) {
        return WalletTransactionType.getByName(name);
    }
}
