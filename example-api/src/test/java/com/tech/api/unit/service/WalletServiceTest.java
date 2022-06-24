package com.tech.api.unit.service;

import com.tech.api.dto.wallet.request.CreateWalletRequestDTO;
import com.tech.api.dto.wallet.request.UpdateWalletRequestDTO;
import com.tech.api.dto.wallet.response.WalletListResponseDTO;
import com.tech.api.dto.wallet.response.WalletResponseDTO;
import com.tech.api.entity.User;
import com.tech.api.entity.Wallet;
import com.tech.api.entity.WalletStatus;
import com.tech.api.repository.WalletRepository;
import com.tech.api.service.impl.WalletServiceImpl;
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
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class WalletServiceTest {

    @InjectMocks
    WalletServiceImpl walletService;

    @Mock
    WalletRepository walletRepository;

    @Test
    public void testGetWallets() {
        Wallet wallet = new Wallet();
        wallet.setId(333L);
        Page<Wallet> walletPage1 = new PageImpl(List.of(wallet));
        Long userId = 15L;
        Mockito.when(walletRepository.findByUserId(userId, PageRequest.of(0, 5))).thenReturn(walletPage1);
        WalletListResponseDTO walletListResponseDTO = walletService.getWallets(userId, 0, 5);
        Assertions.assertEquals(1, walletListResponseDTO.getTotalElementSize());
        Assertions.assertEquals(wallet.getId(), walletListResponseDTO.getWallets().get(0).getId());
    }

    @Test
    public void testGetWallet() {
        Wallet wallet = generateWallet();
        Mockito.when(walletRepository.findOneByUserIdAndId(wallet.getUser().getId(), wallet.getId())).thenReturn(Optional.of(wallet));
        Wallet serviceWallet = walletService.getWallet(wallet.getUser().getId(), wallet.getId());
        Assertions.assertEquals(wallet.getId(), serviceWallet.getId());
    }

    @Test
    public void testGetWalletBySerialNumber() {
        Wallet wallet = generateWallet();
        Mockito.when( walletRepository.findOneBySerialNumber(wallet.getSerialNumber())).thenReturn(Optional.of(wallet));
        Wallet serviceWallet = walletService.getWalletBySerialNumber(wallet.getSerialNumber());
        Assertions.assertEquals(wallet.getSerialNumber(), serviceWallet.getSerialNumber());
    }

    private Wallet generateWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(333L);
        wallet.setName("my wallet");
        wallet.setSerialNumber("323XMX");
        wallet.setUser(generateUser());
        return wallet;
    }

    private User generateUser() {
        User user = new User();
        user.setId(15L);
        return user;
    }

    @Test
    public void testUpdateWallet() {
        Wallet wallet = generateWallet();
        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        UpdateWalletRequestDTO updateWalletRequestDTO = new UpdateWalletRequestDTO();
        updateWalletRequestDTO.setName("my wallet");
        updateWalletRequestDTO.setWalletStatus(WalletStatus.PASSIVE);
        WalletResponseDTO walletResponseDTO = walletService.updateWallet(wallet, updateWalletRequestDTO);
        Assertions.assertEquals(updateWalletRequestDTO.getName(), walletResponseDTO.getName());
        Assertions.assertEquals(updateWalletRequestDTO.getWalletStatus(), walletResponseDTO.getWalletStatus());
    }

    @Test
    public void testCreateWallet() {
        CreateWalletRequestDTO createWalletRequestDTO = new CreateWalletRequestDTO();
        createWalletRequestDTO.setName("my wallet 2");
        Mockito.when(walletRepository.save(new Wallet())).thenReturn(new Wallet());
        Long userId = 115L;
        WalletResponseDTO walletResponseDTO = walletService.createWallet(userId, createWalletRequestDTO);
        Assertions.assertEquals(createWalletRequestDTO.getName(), walletResponseDTO.getName());
        Assertions.assertEquals(BigDecimal.ZERO, walletResponseDTO.getBalance());
    }

}
