package com.example.bankingdemo.exceptions;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.BankResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Hidden //UNBLOCKS SWAGGER URL
public class GlobalExeception {
    ResponseInfo responseInfo = new ResponseInfo();

    //    Gson gson = new Gson();
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BankResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            Object errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        BankResponse response = BankResponse.builder()
                .responseCode(responseInfo.WRONG_INPUT)
                .responseMessage(responseInfo.WRONG_INPUT_MESSAGE)
                .responseData(null)
                .errorDetails(errors)
                .build();


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//        @ExceptionHandler(AccountNotFoundException.class)
//        public ResponseEntity<BankResponse> handleAccountNotFound(AccountNotFoundException ex) {
//            BankResponse response = BankResponse.builder()
//                    .responseCode(ResponseCode.FAILURE)
//                    .responseMessage(ResponseMessage.FAILURE)
//                    .responseData(null)
//                    .timestamp(LocalDateTime.now())
//                    .errorDetails(ex.getMessage())
//                    .build();
//
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BankResponse> handleGenericException(Exception ex) {
        BankResponse response = BankResponse.builder()
                .responseCode(responseInfo.SOMETHING_WENT_WRONG)
                .responseMessage(responseInfo.SOMETHING_WENT_WRONG_MESSAGE)
                .responseData(null)
                .errorDetails("An unexpected error occurred: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
