package com.example.demo.cms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CMSRepository extends JpaRepository <Content, Integer> {
    // implement only custom needs here, CRUD is handled by JPA
}