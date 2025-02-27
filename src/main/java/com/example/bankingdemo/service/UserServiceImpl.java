package com.example.bankingdemo.service;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.AccountInfo;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.dto.UserRequest;
import com.example.bankingdemo.model.User;
import com.example.bankingdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        ResponseInfo responseInfo = new ResponseInfo();
        String fullName = userRequest.getFirstName() + " " + userRequest.getLastName() + " " + userRequest.getOtherName();

//        CHECK IF USER ALREADY EXISTS
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(responseInfo.ACCOUNT_ALREADY_EXISTS)
                    .responseMessage(responseInfo.ACCOUNT_ALREADY_EXISTS_MESSAGE)
                    .responseData(null)
                    .build();
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
                .build();

//        SAVE THE USER TO THE DATABASE
        userRepository.save(user);

        return BankResponse.builder()
                .responseCode(responseInfo.ACCOUNT_CREATED)
                .responseMessage(responseInfo.ACCOUNT_CREATED_MESSAGE)
                .responseData(AccountInfo.builder()
                        .accountName(fullName)
                        .accountNumber(user.getId().toString())
                        .build())
                .build();
    }
}
