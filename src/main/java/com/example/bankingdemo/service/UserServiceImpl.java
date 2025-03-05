package com.example.bankingdemo.service;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.*;
import com.example.bankingdemo.model.User;
import com.example.bankingdemo.repository.TransactionRepository;
import com.example.bankingdemo.repository.UserRepository;
import com.example.bankingdemo.utilities.AccountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountUtil accountUtil;
    @Autowired
    EmailService emailService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionService transactionService;
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

    @Override
    public BankResponse processTransfer(TransferRequest transferRequest) {
//        CHECK FOR RECEIVER ACCOUNT NUMBER
        if (!userRepository.existsByAccountNumber(transferRequest.getReceiverAccountNumber())) {
            return accountUtil.buildErrorResponse(responseInfo.ACCOUNT_DOES_NOT_EXIST, responseInfo.RECEIVER_ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        }

//        CHECK FOR SOURCE ACCOUNT NUMBER
        if (!userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber())) {
            return accountUtil.buildErrorResponse(responseInfo.ACCOUNT_DOES_NOT_EXIST, responseInfo.SOURCE_ACCOUNT_DOES_NOT_EXIST_MESSAGE);
        }

        User userSending = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        User userReceiving = userRepository.findByAccountNumber(transferRequest.getReceiverAccountNumber());

//        CHECK SOURCE ACCOUNT BALANCE
        if (userSending.getAccountBalance().compareTo(transferRequest.getCreditAmount()) < 0) {
            return accountUtil.buildErrorResponse(responseInfo.INSUFFICENT_FUNDS, responseInfo.INSUFFICENT_FUNDS_MESSAGE);
        }

//        DEBIT THE SENDER ACCOUNT BALANCE
        userSending.setAccountBalance(userSending.getAccountBalance().subtract(transferRequest.getCreditAmount()));
        userRepository.save(userSending);

//        SEND DEBIT EMAIL NOTIFICATION TO THE SENDER
        try {
            emailService.sendEmail(Email.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userSending.getEmail())
                    .message("Dear " + userSending.getFirstName() + " " + userSending.getLastName() + ",\n\n" +
                            "Your account has been successfully debited.\n\n" +
                            "The amount debited from your bank account is: " + transferRequest.getCreditAmount() + "\n\n" +
                            "Thank you for banking with us.\n\n" +
                            "Best Regards,\n" +
                            "Banking App Team")
                    .build());
        } catch (Exception e) {
            log.info("Debit Alert not sent" + e.getMessage());
        }

//        CREDIT THE SENDER
        userReceiving.setAccountBalance(userReceiving.getAccountBalance().add(transferRequest.getCreditAmount()));
        userRepository.save(userReceiving);

//        SAVE TRANSACTION HISTORY
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType("CREDIT")
                .accountNumber(transferRequest.getReceiverAccountNumber())
                .amount(transferRequest.getCreditAmount())
                .build();


        transactionService.saveTransaction(transactionRequest);

//        SEND CREDIT EMAIL NOTIFICATION TO THE RECEIVER
        try {
            emailService.sendEmail(Email.builder()
                    .subject("CREDIT ALERT")
                    .recipient(userReceiving.getEmail())
                    .message("Dear " + userReceiving.getFirstName() + " " + userReceiving.getLastName() + ",\n\n" +
                            "Your account has been successfully credited.\n\n" +
                            "The amount credited to your bank account is: " + transferRequest.getCreditAmount() + "\n\n" +
                            "From " + userSending.getFirstName() + " " + userSending.getLastName() +
                            "Thank you for banking with us.\n\n" +
                            "Best Regards,\n" +
                            "Banking App Team")
                    .build());
        } catch (Exception e) {
            log.info("Credit  Alert not sent" + e.getMessage());
        }

        return accountUtil.buildSuccessResponse(responseInfo.TRANSFER_SUCCESS,
                responseInfo.TRANSFER_SUCCESS_MESSAGE,
                userReceiving.getFirstName() + " " + userReceiving.getLastName(),
                userReceiving.getAccountBalance(), userReceiving.getAccountNumber());
    }
}

