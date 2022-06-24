package com.tech.api.controller;

import com.tech.api.dto.ApiResponse;
import com.tech.api.dto.transaction.request.CreateWalletTransactionRequestDTO;
import com.tech.api.dto.transaction.request.SendMoneyRequestDTO;
import com.tech.api.dto.transaction.response.SendMoneyResponseDTO;
import com.tech.api.dto.transaction.response.WalletTransactionListDTO;
import com.tech.api.dto.transaction.response.WalletTransactionResponseDTO;
import com.tech.api.entity.Wallet;
import com.tech.api.security.CurrentUser;
import com.tech.api.security.UserPrincipal;
import com.tech.api.service.WalletService;
import com.tech.api.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/wallets/{walletId}/transactions")
public class WalletTransactionController {

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Autowired
    private WalletService walletService;

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<WalletTransactionListDTO> getTransactions(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("walletId") Long walletId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return new ApiResponse<>(walletTransactionService.getTransactionsByUserIdAndWalletId(currentUser.getId(), walletId, page, size));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<WalletTransactionResponseDTO> createTransaction(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody CreateWalletTransactionRequestDTO createWalletTransactionRequestDTO,
            @PathVariable("walletId") Long walletId
    ) {
        Wallet wallet = walletService.getWallet(currentUser.getId(), walletId);
        return new ApiResponse<>(walletTransactionService.createWalletTransaction(wallet, createWalletTransactionRequestDTO));
    }

    @PostMapping("/send-money")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<SendMoneyResponseDTO> sendMoney(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody SendMoneyRequestDTO sendMoneyRequestDTO,
            @PathVariable("walletId") Long walletId
    ) {
        Wallet fromWallet = walletService.getWallet(currentUser.getId(), walletId);
        Wallet toWallet = walletService.getWalletBySerialNumber(sendMoneyRequestDTO.getTargetWalletSerialNumber());
        return new ApiResponse<>(walletTransactionService.sendMoney(fromWallet, toWallet, sendMoneyRequestDTO));
    }

}
