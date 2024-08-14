package com.example.Bank.repository;

import com.example.Bank.entity.Details;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Details,Long> {
}
