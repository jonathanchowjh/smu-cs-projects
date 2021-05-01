package com.example.demo.user;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.user.User;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findByUsername(String username);
}