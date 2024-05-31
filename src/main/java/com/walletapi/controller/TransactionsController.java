package com.walletapi.controller;

import com.walletapi.dto.*;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.service.TransactionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionsController {

    private final TransactionsService transactionsService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody final TransactionsDTO data) {
        try {
            return ResponseEntity.ok(transactionsService.createTransaction(data));
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Integer id) {
        try {
            TransactionsResponse transaction = transactionsService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @GetMapping("/get/wallet/{walletId}")
    public ResponseEntity<?> getTransactionsByWalletId(@PathVariable Integer walletId) {
        try {
            TransactionsWalletResponse transactionsWalletResponse = transactionsService.getTransactionsByWalletId(walletId);
            return ResponseEntity.ok(transactionsWalletResponse);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Integer id, @RequestBody TransactionsDTO data) {
        try {
            TransactionsDTO updatedTransaction = transactionsService.updateTransaction(id, data);
            return ResponseEntity.ok(updatedTransaction);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Integer id) {
        try {
            transactionsService.deleteTransaction(id);
            return ResponseEntity.ok().build();
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

    @GetMapping("/get/profitability/{walletId}")
    public ResponseEntity<?> getProfitabilityByWalletId(@PathVariable Integer walletId) {
        try {
            List<ProfitabilityDTO> profitabilityWalletResponse = transactionsService.getProfitabilityByWalletId(walletId);
            return ResponseEntity.ok(profitabilityWalletResponse);
        } catch (BadRequestNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ExceptionResponse(exception.getErrorCode(), exception.getMessage()));
        }
    }

}
