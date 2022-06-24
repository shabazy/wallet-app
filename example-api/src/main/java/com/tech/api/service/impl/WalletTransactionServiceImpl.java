package com.tech.api.service.impl;

import com.tech.api.dto.transaction.request.CreateWalletTransactionRequestDTO;
import com.tech.api.dto.transaction.request.SendMoneyRequestDTO;
import com.tech.api.dto.transaction.response.SendMoneyResponseDTO;
import com.tech.api.dto.transaction.response.WalletTransactionListDTO;
import com.tech.api.dto.transaction.response.WalletTransactionResponseDTO;
import com.tech.api.dto.wallet.response.WalletListResponseDTO;
import com.tech.api.entity.WalletTransactionType;
import com.tech.api.entity.Wallet;
import com.tech.api.entity.WalletTransaction;
import com.tech.api.entity.WalletTransactionStatus;
import com.tech.api.exception.BadRequestException;
import com.tech.api.repository.WalletRepository;
import com.tech.api.repository.WalletTransactionRepository;
import com.tech.api.service.WalletTransactionService;
import com.tech.api.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private static final int TRANSACTION_REFERENCE_LENGTH = 12;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public WalletTransactionListDTO getTransactionsByUserIdAndWalletId(
            Long userId,
            Long walletId,
            int page,
            int size
    ) {
        Page<WalletTransaction> walletTransactionPage = walletTransactionRepository.findOneByUserIdAndWalletId(
                userId,
                walletId,
                PageRequest.of(page, size)
        );
        WalletTransactionListDTO walletTransactionListDTO = new WalletTransactionListDTO();
        walletTransactionListDTO.setPage(page);
        walletTransactionListDTO.setSize(size);
        walletTransactionListDTO.setTotalElementSize(walletTransactionPage.getTotalElements());
        walletTransactionListDTO.setTotalPageSize(walletTransactionPage.getTotalPages());
        walletTransactionListDTO.setWalletTransactions(walletTransactionPage.getContent());
        walletTransactionListDTO.setLast(walletTransactionPage.isLast());
        return walletTransactionListDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public WalletTransactionResponseDTO createWalletTransaction(
            Wallet wallet,
            CreateWalletTransactionRequestDTO createWalletTransactionRequestDTO
    ) {
        WalletTransactionType transactionType = createWalletTransactionRequestDTO.getTransactionType();
        BigDecimal amount = createWalletTransactionRequestDTO.getAmount();
        BigDecimal balance = getBalance(wallet, amount, transactionType);
        wallet.setBalance(balance);
        WalletTransaction walletTransaction = prepareWalletTransaction(wallet, createWalletTransactionRequestDTO);
        walletRepository.save(wallet);
        walletTransactionRepository.save(walletTransaction);
        return new WalletTransactionResponseDTO(walletTransaction);
    }

    @Override
    @Transactional(readOnly = false)
    public SendMoneyResponseDTO sendMoney(Wallet wallet, Wallet targetWallet, SendMoneyRequestDTO sendMoneyRequestDTO) {
        if (wallet.getId().equals(targetWallet.getId())) {
            throw new BadRequestException("The transaction cannot be sent from same wallet.");
        }
        CreateWalletTransactionRequestDTO sourceTransactionRequestDTO = new CreateWalletTransactionRequestDTO(WalletTransactionType.PAYMENT, sendMoneyRequestDTO.getAmount());
        sourceTransactionRequestDTO.setDescription(String.format("%s amount transferred to %s", sendMoneyRequestDTO.getAmount(), targetWallet.getSerialNumber()));
        CreateWalletTransactionRequestDTO targetTransactionRequestDTO = new CreateWalletTransactionRequestDTO(WalletTransactionType.TOP_UP, sendMoneyRequestDTO.getAmount());
        targetTransactionRequestDTO.setDescription(String.format("%s amount transferred from %s", sendMoneyRequestDTO.getAmount(), wallet.getSerialNumber()));
        WalletTransactionResponseDTO sourceWalletTransactionResponseDTO = createWalletTransaction(wallet, sourceTransactionRequestDTO);
        WalletTransactionResponseDTO targetWalletTransactionResponseDTO = createWalletTransaction(targetWallet, targetTransactionRequestDTO);
        return new SendMoneyResponseDTO(sourceWalletTransactionResponseDTO, targetWalletTransactionResponseDTO);
    }

    private WalletTransaction prepareWalletTransaction(Wallet wallet, CreateWalletTransactionRequestDTO createWalletTransactionRequestDTO) {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setAmount(createWalletTransactionRequestDTO.getAmount());
        walletTransaction.setDate(new Date());
        walletTransaction.setDescription(createWalletTransactionRequestDTO.getDescription());
        walletTransaction.setReference(StringUtil.generateTransactionReference(TRANSACTION_REFERENCE_LENGTH));
        walletTransaction.setWalletTransactionStatus(WalletTransactionStatus.SUCCEEDED);
        walletTransaction.setTransactionType(createWalletTransactionRequestDTO.getTransactionType());
        return walletTransaction;
    }

    private BigDecimal getBalance(Wallet wallet, BigDecimal amount, WalletTransactionType transactionType) {
        if (WalletTransactionType.TOP_UP.equals(transactionType)) {
            return wallet.getBalance().add(amount);
        } else if (WalletTransactionType.PAYMENT.equals(transactionType)) {
            BigDecimal balance = wallet.getBalance().subtract(amount);
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("The balance cannot be less than zero.");
            }
            return balance;
        }
        throw new BadRequestException("Transaction type is not found. Transaction type: " + transactionType);
    }

}
