package com.example.demo.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository <Account, Integer> {
    // implement only custom needs here, CRUD is handled by JPA
}