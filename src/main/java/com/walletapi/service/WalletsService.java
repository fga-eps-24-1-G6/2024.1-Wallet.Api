package com.walletapi.service;

import com.walletapi.dto.WalletsResponse;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Wallets;
import com.walletapi.repository.WalletsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletsService {

    private final WalletsRepository walletsRepository;

    public WalletsResponse createWallet(String name) {
        try {
            Wallets savedWallet = walletsRepository.save(Wallets.builder()
                    .name(name)
                    .build());

            return WalletsResponse.builder()
                    .id(savedWallet.getId())
                    .name(savedWallet.getName())
                    .build();
        } catch (Exception e) {
            throw new BadRequestNotFoundException(409, "Fail to create the wallet");
        }
    }
}
