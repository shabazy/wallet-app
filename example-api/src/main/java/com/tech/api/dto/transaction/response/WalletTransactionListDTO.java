package com.tech.api.dto.transaction.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.api.entity.WalletTransaction;
import lombok.Data;

import java.util.List;

@Data
public class WalletTransactionListDTO {

    @JsonProperty
    private List<WalletTransaction> walletTransactions;

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
