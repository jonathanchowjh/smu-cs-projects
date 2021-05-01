package com.example.demo.trades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StocksRepository extends JpaRepository <Stock, Integer> {
    // implement only custom needs here, CRUD is handled by JPA
    Optional<Stock> findBySymbol(String symbol);
}