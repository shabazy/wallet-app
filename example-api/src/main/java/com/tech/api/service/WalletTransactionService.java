package com.tech.api.service;

import com.tech.api.dto.transaction.request.CreateWalletTransactionRequestDTO;
import com.tech.api.dto.transaction.request.SendMoneyRequestDTO;
import com.tech.api.dto.transaction.response.SendMoneyResponseDTO;
import com.tech.api.dto.transaction.response.WalletTransactionListDTO;
import com.tech.api.dto.transaction.response.WalletTransactionResponseDTO;
import com.tech.api.entity.Wallet;

public interface WalletTransactionService {

    WalletTransactionListDTO getTransactionsByUserIdAndWalletId(Long userId, Long walletId, int page, int size);

    WalletTransactionResponseDTO createWalletTransaction(Wallet wallet, CreateWalletTransactionRequestDTO createWalletTransactionRequestDTO);

    SendMoneyResponseDTO sendMoney(Wallet wallet, Wallet targetWallet, SendMoneyRequestDTO sendMoneyRequestDTO);

}
