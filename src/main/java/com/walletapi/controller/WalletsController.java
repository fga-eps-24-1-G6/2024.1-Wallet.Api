package com.walletapi.controller;

import com.walletapi.dto.ExceptionResponse;
import com.walletapi.dto.WalletsResponse;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.model.Wallets;
import com.walletapi.service.WalletsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@AllArgsConstructor
public class WalletsController {

    private final WalletsService walletsService;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createWallet(@RequestBody final WalletsResponse data) {
        try {
            return ResponseEntity.ok(walletsService.createWallet(data.getName()));
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }
    @GetMapping("/get-wallets")
    public ResponseEntity<List<WalletsResponse>> getAllWallets() {
        List<WalletsResponse> wallets = walletsService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/get-wallet/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable Integer id) {
        try {
            WalletsResponse wallet = walletsService.getWalletById(id);
            return ResponseEntity.ok(wallet);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @PutMapping("/edit-wallet/{id}")
    public ResponseEntity<?> updateWallet(@PathVariable Integer id, @RequestBody WalletsResponse data) {
        try {
            WalletsResponse updatedWallet = walletsService.updateWallet(id, data.getName());
            return ResponseEntity.ok(updatedWallet);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @DeleteMapping("/delete-wallet/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable Integer id) {
        try {
            walletsService.deleteWallet(id);
            return ResponseEntity.ok().build();
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }
}
