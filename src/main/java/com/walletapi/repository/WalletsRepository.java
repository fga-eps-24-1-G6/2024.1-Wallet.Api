package com.walletapi.repository;

import com.walletapi.model.Wallets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletsRepository extends JpaRepository<Wallets, Integer> {
    @Query("SELECT w FROM Wallets w WHERE w.externalId = :externalId")
    Optional<List<Wallets>> findByExternalId(String externalId);
}
