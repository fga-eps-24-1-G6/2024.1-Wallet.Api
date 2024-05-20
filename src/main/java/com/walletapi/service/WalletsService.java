package com.walletapi.service;

import com.walletapi.dto.WalletsDTO;
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

    public WalletsDTO createWallet(String name, String externalId) {
        try {
            Wallets savedWallet = walletsRepository.save(Wallets.builder()
                    .name(name)
                    .externalId(externalId)
                    .build());

            return WalletsDTO.builder()
                    .id(savedWallet.getId())
                    .name(savedWallet.getName())
                    .externalId(savedWallet.getExternalId())
                    .build();
        } catch (Exception e) {
            throw new BadRequestNotFoundException(409, "Fail to create the wallet");
        }
    }

    public List<WalletsDTO> getAllWallets() {
        List<Wallets> wallets = walletsRepository.findAll();
        return wallets.stream()
                .map(wallet -> WalletsDTO.builder()
                        .id(wallet.getId())
                        .name(wallet.getName())
                        .externalId(wallet.getExternalId())
                        .build())
                .collect(Collectors.toList());
    }

    public WalletsDTO getWalletById(Integer id) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));

        return WalletsDTO.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .externalId(wallet.getExternalId())
                .build();
    }

    public WalletsDTO updateWallet(Integer id, String name, String externalId) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));

        wallet.setName(name);
        wallet.setExternalId(externalId);
        Wallets updatedWallet = walletsRepository.save(wallet);

        return WalletsDTO.builder()
                .id(updatedWallet.getId())
                .name(updatedWallet.getName())
                .externalId(updatedWallet.getExternalId())
                .build();
    }

    public void deleteWallet(Integer id) {
        Wallets wallet = walletsRepository.findById(id)
                .orElseThrow(() -> new BadRequestNotFoundException(404, "Carteira não encontrada com o ID: " + id));
        walletsRepository.delete(wallet);
    }
}
