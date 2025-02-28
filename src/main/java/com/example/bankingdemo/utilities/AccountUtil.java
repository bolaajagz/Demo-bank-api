package com.example.bankingdemo.utilities;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.AccountInfo;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountUtil {
    @Autowired
    UserRepository userRepository;

    public BankResponse ExistingUser(String email, String phoneNumber) {
        ResponseInfo responseInfo = new ResponseInfo();

//        CHECK IF USER ALREADY EXISTS
        if (userRepository.existsByEmailOrPhoneNumber(email, phoneNumber)) {
            return BankResponse.builder()
                    .responseCode(responseInfo.ACCOUNT_ALREADY_EXISTS)
                    .responseMessage(responseInfo.ACCOUNT_ALREADY_EXISTS_MESSAGE)
                    .responseData(null)
                    .build();
        }
        return null;
    }

    public long generateAccountNumber() {
        return (long) (Math.random() * 9000000000L) + 1000000000L;
    }

    public String generateAccountName(String firstName, String lastName, String otherName) {
        return firstName + " " + lastName + " " + otherName;
    }

    public BankResponse buildErrorResponse(String responseCode, String responseMessage) {
        return BankResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .responseData(null)
                .build();
    }

    public BankResponse buildSuccessResponse(String responseCode, String responseMessage, String accountName, BigDecimal accountBalnce, String accountNumber) {
        return BankResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .responseData(AccountInfo.builder()
                        .accountName(accountName)
                        .accountBalance(accountBalnce)
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }
}
