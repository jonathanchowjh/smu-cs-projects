package com.example.demo.trades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository <Trade, Integer> {
    // implement only custom needs here, CRUD is handled by JPA
    Optional<Trade> findBySymbol(String symbol);
    Iterable<Trade> findAllBySymbol(String symbol);
}