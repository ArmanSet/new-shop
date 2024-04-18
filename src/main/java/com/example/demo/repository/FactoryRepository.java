package com.example.demo.repository;

import com.example.demo.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository   extends JpaRepository<Factory,Long> {
    Factory findByName(String name);
}
