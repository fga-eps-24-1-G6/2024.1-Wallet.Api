package com.walletapi.repository;

import com.walletapi.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

    @Query("SELECT t FROM Transactions t WHERE t.walletId = :walletId")
    Optional<List<Transactions>> getTransactionsByWalletId(@Param("walletId") Integer walletId);

}
