package com.tech.api.unit.service;

import com.tech.api.dto.transaction.request.CreateWalletTransactionRequestDTO;
import com.tech.api.dto.transaction.request.SendMoneyRequestDTO;
import com.tech.api.dto.transaction.response.SendMoneyResponseDTO;
import com.tech.api.dto.transaction.response.WalletTransactionListDTO;
import com.tech.api.dto.transaction.response.WalletTransactionResponseDTO;
import com.tech.api.entity.*;
import com.tech.api.repository.WalletRepository;
import com.tech.api.repository.WalletTransactionRepository;
import com.tech.api.service.impl.WalletTransactionServiceImpl;
import com.tech.api.util.StringUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
public class WalletTransactionServiceTest {

    @InjectMocks
    private WalletTransactionServiceImpl walletTransactionService;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Test
    public void testGetTransactionsByUserIdAndWalletId() {
        WalletTransaction walletTransaction = generateWalletTransaction();
        Wallet wallet = walletTransaction.getWallet();
        Page<WalletTransaction> walletTransactions = new PageImpl<>(List.of(walletTransaction));
        Mockito.when(walletTransactionRepository.findOneByUserIdAndWalletId(
                wallet.getUser().getId(),
                wallet.getId(),
                PageRequest.of(0, 5)
        )).thenReturn(walletTransactions);
        WalletTransactionListDTO walletTransactionListDTO = walletTransactionService
                .getTransactionsByUserIdAndWalletId(wallet.getUser().getId(), wallet.getId(), 0, 5);
        Assertions.assertEquals(1, walletTransactionListDTO.getTotalElementSize());
        Assertions.assertEquals(walletTransaction.getAmount(), walletTransactionListDTO.getWalletTransactions().get(0).getAmount());
        Assertions.assertEquals(walletTransaction.getReference(), walletTransactionListDTO.getWalletTransactions().get(0).getReference());
    }

    @Test
    public void testCreateWalletTransaction() {
        Wallet wallet = generateWallet(generateUser());
        wallet.setBalance(BigDecimal.valueOf(150.22));
        CreateWalletTransactionRequestDTO createWalletTransactionRequestDTO = generateWalletTransactionRequest();
        WalletTransaction walletTransaction = generateWalletTransaction();
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        Mockito.when(walletTransactionRepository.save(walletTransaction)).thenReturn(walletTransaction);
        WalletTransactionResponseDTO walletTransactionResponseDTO = walletTransactionService
                .createWalletTransaction(wallet, createWalletTransactionRequestDTO);
        Assertions.assertEquals(createWalletTransactionRequestDTO.getTransactionType().getName(), walletTransactionResponseDTO.getTransactionType());
        Assertions.assertEquals(createWalletTransactionRequestDTO.getAmount(), walletTransactionResponseDTO.getAmount());
    }

    @Test
    public void testSendMoney() {
        Wallet sourceWallet = generateWallet(generateUser());
        sourceWallet.setBalance(BigDecimal.valueOf(150.20));
        Wallet targetWallet = generateWallet(generateUser());
        targetWallet.setBalance(BigDecimal.valueOf(30.12));
        SendMoneyRequestDTO sendMoneyRequestDTO = new SendMoneyRequestDTO();
        sendMoneyRequestDTO.setTargetWalletSerialNumber(targetWallet.getSerialNumber());
        sendMoneyRequestDTO.setAmount(BigDecimal.valueOf(25.22));
        SendMoneyResponseDTO sendMoneyResponseDTO = walletTransactionService.sendMoney(sourceWallet, targetWallet, sendMoneyRequestDTO);
        Assertions.assertEquals(sendMoneyResponseDTO.getSource().getAmount(), sendMoneyResponseDTO.getTarget().getAmount());
        Assertions.assertEquals(sendMoneyResponseDTO.getSource().getTransactionType(), WalletTransactionType.PAYMENT.getName());
        Assertions.assertEquals(sendMoneyResponseDTO.getTarget().getTransactionType(), WalletTransactionType.TOP_UP.getName());
    }

    private CreateWalletTransactionRequestDTO generateWalletTransactionRequest() {
        return new CreateWalletTransactionRequestDTO(WalletTransactionType.PAYMENT, BigDecimal.valueOf(10.50));
    }

    private WalletTransaction generateWalletTransaction() {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setReference("AXS123");
        walletTransaction.setDate(new Date());
        walletTransaction.setWallet(generateWallet(generateUser()));
        walletTransaction.setWalletTransactionStatus(WalletTransactionStatus.SUCCEEDED);
        walletTransaction.setDescription("transaction desc");
        walletTransaction.setAmount(BigDecimal.valueOf(10.22));
        walletTransaction.setTransactionType(WalletTransactionType.TOP_UP);
        return walletTransaction;
    }

    private Wallet generateWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setName("my wallet");
        wallet.setSerialNumber(StringUtil.generateTransactionReference(5));
        wallet.setId((new Random()).nextLong());
        wallet.setUser(user);
        return wallet;
    }

    private User generateUser() {
        Long userId = 111L;
        User user = new User();
        user.setId(userId);
        return user;
    }

}
