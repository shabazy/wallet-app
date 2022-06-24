package com.tech.api.repository;

import com.tech.api.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends PagingAndSortingRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId AND w.walletStatus = 'ACTIVE' ORDER BY w.createDate DESC")
    Page<Wallet> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId and w.id = :walletId AND w.walletStatus = 'ACTIVE'")
    Optional<Wallet> findOneByUserIdAndId(@Param("userId") Long userId, @Param("walletId") Long walletId);

    @Query("SELECT w FROM Wallet w WHERE w.serialNumber = :serialNumber AND w.walletStatus = 'ACTIVE'")
    Optional<Wallet> findOneBySerialNumber(@Param("serialNumber") String serialNumber);

}
