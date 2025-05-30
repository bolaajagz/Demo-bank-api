package com.example.bankingdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
    private LocalDate createdAt;
}
