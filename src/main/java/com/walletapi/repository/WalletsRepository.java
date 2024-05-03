package com.walletapi.repository;

import com.walletapi.model.Wallets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletsRepository extends JpaRepository<Wallets, Integer> {
}
