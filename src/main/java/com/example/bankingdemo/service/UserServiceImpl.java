package com.example.bankingdemo.service;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.AccountInfo;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.dto.Email;
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

    @Autowired
    EmailService emailService;
    ResponseInfo responseInfo = new ResponseInfo();

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        String fullName = accountUtil.generateAccountName(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getOtherName());
        String accountNumber = String.valueOf(accountUtil.generateAccountNumber());

//        CHECK IF USER ALREADY EXISTS EMAIL OR PHONE NUMBER
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
        User savedUser = userRepository.save(user);

//        SEND EMAIL TO USER ALERT OF ACCOUNT CREATION
        emailService.sendEmail(Email.builder()
                .subject("Account Creation")
                .recipient(savedUser.getEmail())
                .message("Dear " + savedUser.getFirstName() + " " + savedUser.getLastName() + ",\n\n" +
                        "Your account has been successfully created.\n\n" +
                        "Your account number is: " + savedUser.getAccountNumber() + "\n\n" +
                        "Thank you for banking with us.\n\n" +
                        "Best Regards,\n" +
                        "Banking App Team")
                .build());

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

    @Override
    public BankResponse getAccountDetails(String accountNumber) {

        if (!userRepository.existsByAccountNumber(accountNumber)) {
            return accountUtil.buildErrorResponse(responseInfo.ACCOUNT_DOES_NOT_EXIST, responseInfo.ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        }

        User users = userRepository.findByAccountNumber(accountNumber);
        return accountUtil.buildSuccessResponse(responseInfo.ACCOUNT_BY_ACCOUNT_NUMBER_SUCCESS,
                responseInfo.ACCOUNT_BY_ACCOUNT_NUMBER_SUCCESS_MESSAGE,
                users.getFirstName() + " " + users.getLastName(),
                users.getAccountBalance(), users.getAccountNumber());

//                BankResponse.builder()
//                .responseCode(responseInfo.ACCOUNT_BY_ACCOUNT_NUMBER_SUCCESS)
//                .responseMessage(responseInfo.ACCOUNT_BY_ACCOUNT_NUMBER_SUCCESS_MESSAGE)
//                .responseData(AccountInfo.builder()
//                        .accountName(users.getFirstName() + " " + users.getLastName())
//                        .accountBalance(users.getAccountBalance())
//                        .accountNumber(users.getAccountNumber())
//                        .build())
//                .build();
    }

    @Override
    public BankResponse getAccountDetailsWithFullname(String firstname, String lastname, String othername) {
        if (!userRepository.existsByFirstNameAndLastNameAndOtherName(firstname, lastname, othername)) {
            return accountUtil.buildErrorResponse(responseInfo.ACCOUNT_DOES_NOT_EXIST, responseInfo.ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        }

        User users = userRepository.findByFirstNameAndLastNameAndOtherName(firstname, lastname, othername);
        return accountUtil.buildSuccessResponse(responseInfo.ACCOUNT_BY_ACCOUNT_NUMBER_SUCCESS,
                responseInfo.ACCOUNT_BY_ACCOUNT_NUMBER_SUCCESS_MESSAGE,
                users.getFirstName() + " " + users.getLastName() + " " + users.getOtherName(),
                users.getAccountBalance(), users.getAccountNumber());
    }
}

