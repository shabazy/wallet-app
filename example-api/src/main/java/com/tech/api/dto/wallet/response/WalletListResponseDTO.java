package com.tech.api.dto.wallet.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.api.entity.Wallet;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WalletListResponseDTO implements Serializable {

    @JsonProperty
    private List<Wallet> wallets;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;

    @JsonProperty("total_element_size")
    private long totalElementSize;

    @JsonProperty("total_page_size")
    private int totalPageSize;

    @JsonProperty("last")
    private boolean last;

}
