package com.example.bankingdemo.service;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.AccountInfo;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.dto.UserRequest;
import com.example.bankingdemo.model.User;
import com.example.bankingdemo.repository.UserRepository;
import com.example.bankingdemo.utilities.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountUtil accountUtil;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        ResponseInfo responseInfo = new ResponseInfo();
        String fullName = accountUtil.generateAccountName(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getOtherName());
        String accountNumber = String.valueOf(accountUtil.generateAccountNumber());

//        CHECK IF USER ALREADY EXISTS
        BankResponse existingUserResponse = accountUtil.ExistingUser(userRequest.getEmail(), userRequest.getPhoneNumber());
        if (existingUserResponse != null) {
            return existingUserResponse;
        }


//        SET REQUEST DATA TO USER OBJECT
        new User();
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .altPhoneNumber(userRequest.getAltPhoneNumber())
                .status("ACTIVE")
                .accountBalance(BigDecimal.ZERO)
                .accountNumber(accountNumber)
                .build();


//        SAVE THE USER TO THE DATABASE
        userRepository.save(user);

        return BankResponse.builder()
                .responseCode(responseInfo.ACCOUNT_CREATED)
                .responseMessage(responseInfo.ACCOUNT_CREATED_MESSAGE)
                .responseData(AccountInfo.builder()
                        .accountName(fullName)
                        .accountNumber(accountNumber)
                        .accountBalance(BigDecimal.ZERO)
                        .build())
                .build();
    }
}
