package com.example.bankingdemo.service;

import com.example.bankingdemo.model.Transaction;
import com.example.bankingdemo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BankStatement {
    //    retrive list of transactions for a user within a specified period of time for using the account number
//    generate pdf file of that transacions
//    send the pdf file to the user email
    @Autowired
    TransactionRepository transactionRepository;

    public List<Transaction> getTransactions(String accountNumber, String startDate, String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        // Convert LocalDate to LocalDateTime at the start of the day
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atStartOfDay();


        return transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber, start, end);

    }
}
