package com.example.bankingdemo.service;

import com.example.bankingdemo.constants.ResponseInfo;
import com.example.bankingdemo.dto.TransactionRequest;
import com.example.bankingdemo.model.User;
import com.example.bankingdemo.repository.TransactionRepository;
import com.example.bankingdemo.repository.UserRepository;
import com.example.bankingdemo.utilities.AccountUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Slf4j
public class BankStatement {
    //    retrieve list of transactions for a user within a specified period of time for using the account number
//    generate pdf file of that transactions
//    send the pdf file to the user email
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountUtil accountUtil;

    @Autowired
    UserRepository userRepository;

    private static final String filePath = "C:\\Users\\aajagunna\\Documents\\statement.pdf";

    public ResponseEntity<?> getTransactions(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {
        ResponseInfo responseInfo = new ResponseInfo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName();

        try {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            List<TransactionRequest> transactions = transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber, start, end);

            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(accountUtil.buildErrorResponse(
                                responseInfo.NO_RECORD_FOUND_BETWEEN_DURATION,
                                responseInfo.NO_RECORD_FOUND_BETWEEN_DURATION_MESSAGE
                        ));
            }

            // Generate pdf file
            Rectangle pageSize = new Rectangle(PageSize.A4);
            Document document = new Document(pageSize);
            log.info("setting the size");
            OutputStream outputStream = new FileOutputStream(filePath);

            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            PdfPTable table = new PdfPTable(1);
            PdfPCell cellHeader = new PdfPCell(new Phrase("Bola Ajagz Bank", font));
            cellHeader.setBorder(0);
            cellHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellHeader.setPadding(20f);

            PdfPCell addressCell = new PdfPCell(new Phrase("Lagos, Nigeria", font));
            addressCell.setBorder(0);

            table.addCell(cellHeader);
            table.addCell(addressCell);

            PdfPTable statementInfo = new PdfPTable(2);
            PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date :" + start, font));
            customerInfo.setBorder(0);
            PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT", font));
            statement.setBorder(0);
            PdfPCell customerInfo2 = new PdfPCell(new Phrase("End Date :" + end, font));
            customerInfo2.setBorder(0);
            PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName, font));
            name.setBorder(0);
            PdfPCell space = new PdfPCell();
            space.setBorder(0);
            PdfPCell address = new PdfPCell(new Phrase("Address: " + user.getAddress(), font));
            address.setBorder(0);

            PdfPTable transactionTable = new PdfPTable(4);
            PdfPCell date = new PdfPCell(new Phrase("DATE", font));
            date.setBackgroundColor(BaseColor.LIGHT_GRAY);
            date.setBorder(0);
            PdfPCell description = new PdfPCell(new Phrase("TRANSACTION TYPE", font));
            description.setBackgroundColor(BaseColor.LIGHT_GRAY);
            description.setBorder(0);
            PdfPCell amount = new PdfPCell(new Phrase("AMOUNT", font));
            amount.setBackgroundColor(BaseColor.LIGHT_GRAY);
            amount.setBorder(0);
            PdfPCell status = new PdfPCell(new Phrase("STATUS", font));
            status.setBackgroundColor(BaseColor.LIGHT_GRAY);
            status.setBorder(0);

            transactionTable.addCell(date);
            transactionTable.addCell(description);
            transactionTable.addCell(amount);
            transactionTable.addCell(status);

            transactions.forEach(transaction -> {
                PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString()));
                PdfPCell descriptionCell = new PdfPCell(new Phrase(transaction.getTransactionType()));
                PdfPCell amountCell = new PdfPCell(new Phrase(transaction.getAmount().toString()));
                PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus()));

                transactionTable.addCell(dateCell);
                transactionTable.addCell(descriptionCell);
                transactionTable.addCell(amountCell);
                transactionTable.addCell(statusCell);
            });

            statementInfo.addCell(customerInfo);
            statementInfo.addCell(statement);
            statementInfo.addCell(customerInfo2);
            statementInfo.addCell(name);
            statementInfo.addCell(space);
            statementInfo.addCell(address);


//            add table to document
            document.add(table);
            document.add(statementInfo);
            document.add(transactionTable);

            document.close();

            return ResponseEntity.ok(transactions);

        } catch (DateTimeParseException e) {
            // Handle invalid date format
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(accountUtil.buildErrorResponse(
                            responseInfo.INVALID_DATE_FORMAT,
                            responseInfo.INVALID_DATE_FORMAT_MESSAGE
                    ));
        }


    }

//    private void generatePdf(List<Transaction> transactions) throws FileNotFoundException, DocumentException {
//
//
//
//        }
}

