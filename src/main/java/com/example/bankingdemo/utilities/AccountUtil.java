package com.example.bankingdemo.utilities;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
