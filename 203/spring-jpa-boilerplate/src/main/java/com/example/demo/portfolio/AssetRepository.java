package com.example.demo.portfolio;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AssetRepository extends CrudRepository<Asset, Integer> {
  Iterable<Asset> findAllByCustomerId(int customerId);
  Iterable<Asset> findAllByCode(String code);

  // find assets associated with particular portfolio
  // Optional<List<Asset>> findByPortfolio(Portfolio portfolio);
}