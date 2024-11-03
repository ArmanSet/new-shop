package com.example.demo.repository;

import com.example.demo.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository  extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findByCategoryId(Long id);
}
