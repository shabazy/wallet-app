package com.tech.api.service.impl;

import com.tech.api.dto.wallet.request.CreateWalletRequestDTO;
import com.tech.api.dto.wallet.request.UpdateWalletRequestDTO;
import com.tech.api.dto.wallet.response.WalletListResponseDTO;
import com.tech.api.dto.wallet.response.WalletResponseDTO;
import com.tech.api.entity.User;
import com.tech.api.entity.Wallet;
import com.tech.api.exception.ResourceNotFoundException;
import com.tech.api.repository.WalletRepository;
import com.tech.api.service.WalletService;
import com.tech.api.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private static final BigDecimal WALLET_INITIAL_BALANCE = BigDecimal.ZERO;
    private static final int WALLET_SERIAL_NUMBER_LENGTH = 8;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public WalletListResponseDTO getWallets(Long userId, int page, int size) {

        Page<Wallet> walletPage = walletRepository.findByUserId(userId, PageRequest.of(page, size));
        WalletListResponseDTO walletListResponseDTO = new WalletListResponseDTO();
        walletListResponseDTO.setPage(page);
        walletListResponseDTO.setSize(size);
        walletListResponseDTO.setTotalElementSize(walletPage.getTotalElements());
        walletListResponseDTO.setTotalPageSize(walletPage.getTotalPages());
        walletListResponseDTO.setWallets(walletPage.getContent());
        walletListResponseDTO.setLast(walletPage.isLast());
        return walletListResponseDTO;
    }

    @Override
    public Wallet getWallet(Long userId, Long walletId) {
        Optional<Wallet> optionalWallet = walletRepository.findOneByUserIdAndId(userId, walletId);
        if (optionalWallet.isEmpty()) {
            throw new ResourceNotFoundException("The wallet is not found with the id: " + walletId);
        }
        return optionalWallet.get();
    }

    @Override
    public Wallet getWalletBySerialNumber(String serialNumber) {
        Optional<Wallet> optionalWallet = walletRepository.findOneBySerialNumber(serialNumber);
        if (optionalWallet.isEmpty()) {
            throw new ResourceNotFoundException("The wallet is not found with the serial number: " + serialNumber);
        }
        return optionalWallet.get();
    }

    @Override
    @Transactional(readOnly = false)
    public WalletResponseDTO updateWallet(Wallet wallet, UpdateWalletRequestDTO updateWalletRequestDTO) {
        wallet.setName(updateWalletRequestDTO.getName());
        wallet.setWalletStatus(updateWalletRequestDTO.getWalletStatus());
        walletRepository.save(wallet);
        return new WalletResponseDTO(wallet);
    }

    @Override
    @Transactional(readOnly = false)
    public WalletResponseDTO createWallet(Long userId, CreateWalletRequestDTO createWalletRequestDTO) {
        Wallet wallet = new Wallet();
        wallet.setName(createWalletRequestDTO.getName());
        wallet.setSerialNumber(StringUtil.generateTransactionReference(WALLET_SERIAL_NUMBER_LENGTH));
        wallet.setBalance(WALLET_INITIAL_BALANCE);
        User user = new User();
        user.setId(userId);
        wallet.setUser(user);
        walletRepository.save(wallet);
        return new WalletResponseDTO(wallet);
    }

}
