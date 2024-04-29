package com.walletapi.controller;

import com.walletapi.dto.ExceptionResponse;
import com.walletapi.dto.WalletsResponse;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.service.WalletsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
}
