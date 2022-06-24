package com.tech.api.repository;

import com.tech.api.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends PagingAndSortingRepository<WalletTransaction, Long> {

    @Query("SELECT wt " +
            "FROM WalletTransaction wt " +
            "JOIN Wallet w ON wt.wallet.id = w.id " +
            "WHERE w.user.id = :userId and w.id = :walletId AND w.walletStatus = 'ACTIVE' " +
            "ORDER BY wt.createDate DESC")
    Page<WalletTransaction> findOneByUserIdAndWalletId(@Param("userId") Long userId, @Param("walletId") Long walletId, Pageable pageable);

}
