package com.example.demo.repository;


import com.example.demo.entity.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountantRepository  extends JpaRepository<Accountant,Long> {
}
