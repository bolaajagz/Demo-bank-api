package com.example.bankingdemo.repository;

import com.example.bankingdemo.dto.TransactionRequest;
import com.example.bankingdemo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query("select new com.example.bankingdemo.dto.TransactionRequest(t.transactionType, t.amount, t.accountNumber, t.status, t.createdAt)" + "from Transaction t where t.accountNumber = :accountNumber and t.createdAt between :startDate and :endDate")
    List<TransactionRequest> findByAccountNumberAndCreatedAtBetween(@Param("accountNumber") String accountNumber,
                                                                    @Param("startDate") LocalDate startDate,
                                                                    @Param("endDate") LocalDate endDate);
}
