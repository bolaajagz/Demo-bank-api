package com.example.bankingdemo.repository;

import com.example.bankingdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //  check if user already exists by email or phone number
    Boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
}
