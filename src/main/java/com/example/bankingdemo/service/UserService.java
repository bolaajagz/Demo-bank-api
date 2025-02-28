package com.example.bankingdemo.service;

import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.dto.TransferRequest;
import com.example.bankingdemo.dto.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse getAccountDetails(String accountNumber);

    BankResponse getAccountDetailsWithFullname(String firstname, String lastname, String othername);

    BankResponse processTransfer(TransferRequest transferRequest);
//    BankResponse withdrawAmount(String accountNumber, double amount);
//    BankResponse transferAmount(String fromAccountNumber, String toAccountNumber, double amount);
//    BankResponse deleteAccount(String accountNumber);
}
