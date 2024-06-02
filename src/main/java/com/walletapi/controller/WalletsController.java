package com.walletapi.controller;

import com.walletapi.dto.*;
import com.walletapi.exception.BadRequestNotFoundException;
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

    @PostMapping("/create")
    public ResponseEntity<?> createWallet(@RequestBody final CreateWalletDTO data) {
        try {
            return ResponseEntity.ok(walletsService.createWallet(data.getName(), data.getExternalId()));
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }
    @GetMapping("/get")
    public ResponseEntity<List<GetWalletsDTO>> getAllWallets() {
        List<GetWalletsDTO> wallets = walletsService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<?> getWalletByExternalId(@PathVariable String id) {
        try {
            List<GetWalletsDTO> wallet = walletsService.getWalletsByExternalId(id);
            return ResponseEntity.ok(wallet);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable Integer id) {
        try {
            GetWalletDTO wallet = walletsService.getWalletById(id);
            return ResponseEntity.ok(wallet);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateWallet(@PathVariable Integer id, @RequestBody final CreateWalletDTO data) {
        try {
            GetWalletDTO updatedWallet = walletsService.updateWallet(id, data.getName());
            return ResponseEntity.ok(updatedWallet);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
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
