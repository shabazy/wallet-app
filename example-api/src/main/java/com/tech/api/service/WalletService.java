package com.tech.api.service;

import com.tech.api.dto.wallet.request.CreateWalletRequestDTO;
import com.tech.api.dto.wallet.request.UpdateWalletRequestDTO;
import com.tech.api.dto.wallet.response.WalletListResponseDTO;
import com.tech.api.dto.wallet.response.WalletResponseDTO;
import com.tech.api.entity.Wallet;

public interface WalletService {

    WalletListResponseDTO getWallets(Long userId, int page, int size);

    Wallet getWallet(Long userId, Long walletId);

    Wallet getWalletBySerialNumber(String serialNumber);

    WalletResponseDTO createWallet(Long userId, CreateWalletRequestDTO createWalletRequestDTO);

    WalletResponseDTO updateWallet(Wallet wallet, UpdateWalletRequestDTO updateWalletRequestDTO);

}
