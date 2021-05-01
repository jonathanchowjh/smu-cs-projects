package com.example.demo.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfersRepository extends JpaRepository <Transfer, Integer> {
    // implement only custom needs here, CRUD is handled by JPA
}
