package com.example.bankingdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {
    private String receiverAccountNumber;
    private String sourceAccountNumber;
    private BigDecimal creditAmount;
}


//    process of transfer should be debit source account then credit destination account

//check if destination account exists

//        check if current balance of source account is not lower than amout to transfer
//
//        if all check pass, subtract amount to transfer from source, add to destination account balance
//        save new account balance
//        send email alert to both parties
//
//        transafer request{
//        receiver account number
//        amount to send