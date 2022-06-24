package com.tech.api.controller;

import com.tech.api.dto.ApiResponse;
import com.tech.api.dto.wallet.request.CreateWalletRequestDTO;
import com.tech.api.dto.wallet.request.UpdateWalletRequestDTO;
import com.tech.api.dto.wallet.response.WalletListResponseDTO;
import com.tech.api.dto.wallet.response.WalletResponseDTO;
import com.tech.api.entity.Wallet;
import com.tech.api.security.CurrentUser;
import com.tech.api.security.UserPrincipal;
import com.tech.api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WalletListResponseDTO> getWallets(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return new ApiResponse<>(walletService.getWallets(currentUser.getId(), page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WalletResponseDTO> getWallet(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("id") Long id
    ) {
        return new ApiResponse<>(new WalletResponseDTO(walletService.getWallet(currentUser.getId(), id)));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WalletResponseDTO> createWallet(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody CreateWalletRequestDTO createWalletRequestDTO
    ) {
        return new ApiResponse<>(walletService.createWallet(currentUser.getId(), createWalletRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<WalletResponseDTO> updateWallet(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateWalletRequestDTO updateWalletRequestDTO
    ) {
        Wallet wallet = walletService.getWallet(currentUser.getId(), id);
        return new ApiResponse<>(walletService.updateWallet(wallet, updateWalletRequestDTO));
    }

}
