package com.example.bankingdemo.controller;

import com.example.bankingdemo.dto.BalanceEnquiry;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.dto.TransferRequest;
import com.example.bankingdemo.dto.UserRequest;
import com.example.bankingdemo.model.Transaction;
import com.example.bankingdemo.service.BankStatement;
import com.example.bankingdemo.service.UserService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "post", description = "User Account Management")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    BankStatement bankStatement;

    @Operation(summary = "Create a new account", description = "This endpoint creates a new account for a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserRequest.class))}),
            @ApiResponse(responseCode = "404", description = "User already exists", content = @Content)})
    @PostMapping("/account")
    public ResponseEntity<BankResponse> createAccount(@Valid @RequestBody UserRequest userRequest) {
//        return userService.createAccount(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAccount(userRequest));

    }

    @Operation(summary = "Get Account Details", description = "This endpoint retrieves user's account details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BalanceEnquiry.class))}),
            @ApiResponse(responseCode = "404", description = "Ooops! Account does not exist", content = @Content)})
    @GetMapping("/account-details/{accountNumber}")
    public ResponseEntity<BankResponse> getAccountDetails(@PathVariable String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAccountDetails(accountNumber));
    }

    @Operation(summary = "Get Account Details by fullname ", description = "This endpoint retrieves user's account details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BalanceEnquiry.class))}),
            @ApiResponse(responseCode = "404", description = "Ooops! Account does not exist", content = @Content)})
    @GetMapping("/account-details-by-fullname")
    public ResponseEntity<BankResponse> getAccountDetailsWithFullname(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String othername) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAccountDetailsWithFullname(firstname, lastname, othername));
    }

    @Operation(summary = "Transfer  ", description = "This endpoint credit and debit a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransferRequest.class))}),
            @ApiResponse(responseCode = "404", description = "Chai! Money no dy your AZA", content = @Content)})
    @PostMapping("/account/transfer")
    public ResponseEntity<BankResponse> processTransfer(@RequestBody TransferRequest transferRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.processTransfer(transferRequest));
    }

    @Operation(summary = "Bank Statement  ", description = "This endpoint to get bank statement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "404", description = "Ooops! No transaction found between given dates", content = @Content)})
    @GetMapping("/account/bank-statement")
    public ResponseEntity<ResponseEntity<?>> processTransfer(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(bankStatement.getTransactions(accountNumber, startDate, endDate));
    }
}
