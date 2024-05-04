package com.walletapi.controller;

import com.walletapi.dto.ExceptionResponse;
import com.walletapi.dto.TransactionsDTO;
import com.walletapi.dto.TransactionsResponse;
import com.walletapi.dto.WalletsDTO;
import com.walletapi.exception.BadRequestNotFoundException;
import com.walletapi.service.TransactionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
