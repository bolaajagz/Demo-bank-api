package com.example.bankingdemo.service;

import com.example.bankingdemo.dto.TransactionRequest;
import org.springframework.stereotype.Component;

@Component
public interface TransactionService {
    void saveTransaction(TransactionRequest transactionRequest);
}
