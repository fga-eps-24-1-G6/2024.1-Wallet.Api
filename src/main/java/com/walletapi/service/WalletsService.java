package com.walletapi.service;

import com.walletapi.dto.WalletsResponse;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Wallets;
import com.walletapi.repository.WalletsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<WalletsResponse> getAllWallets() {
        List<Wallets> wallets = walletsRepository.findAll();
        return wallets.stream()
                .map(wallet -> WalletsResponse.builder()
                        .id(wallet.getId())
                        .name(wallet.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public WalletsResponse getWalletById(Integer id) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));

        return WalletsResponse.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .build();
    }

    public WalletsResponse updateWallet(Integer id, String name) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));

        wallet.setName(name);
        Wallets updatedWallet = walletsRepository.save(wallet);

        return WalletsResponse.builder()
                .id(updatedWallet.getId())
                .name(updatedWallet.getName())
                .build();
    }

    public void deleteWallet(Integer id) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));
        walletsRepository.delete(wallet);
    }
}
