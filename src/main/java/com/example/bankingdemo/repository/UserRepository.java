package com.example.bankingdemo.repository;

import com.example.bankingdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);

    Boolean existsByFirstNameAndLastNameAndOtherName(String firstname, String lastname, String othername);

    User findByFirstNameAndLastNameAndOtherName(String firstname, String lastname, String othername);
}
