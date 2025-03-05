package com.example.bankingdemo.repository;

import com.example.bankingdemo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumberAndCreatedAtBetween(String accountNumber, LocalDate startDate, LocalDate endDate);

//    @Query("SELECT * FROM transaction WHERE account_number = ?1 AND created_at BETWEEN ?2 AND ?3")
//    Collection<Transaction> getTransactionByAccountNumberAndCreatedAtDateBetween();
}
