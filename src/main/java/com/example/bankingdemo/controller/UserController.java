package com.example.bankingdemo.controller;

import com.example.bankingdemo.dto.BalanceEnquiry;
import com.example.bankingdemo.dto.BankResponse;
import com.example.bankingdemo.dto.UserRequest;
import com.example.bankingdemo.service.UserService;
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

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "post", description = "User Account Management")
public class UserController {

    @Autowired
    UserService userService;

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
}
