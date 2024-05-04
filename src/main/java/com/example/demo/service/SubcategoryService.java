package com.example.demo.service;

import com.example.demo.entity.Products;
import com.example.demo.entity.Subcategory;
import com.example.demo.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;

    public void save(Subcategory subcategory) {
        subcategoryRepository.save(subcategory);
    }

    public Subcategory getSubcategoryById(Long categoryID) {
        return subcategoryRepository.findById(categoryID).orElse(null);
    }

    public List<Subcategory> findAll() {
        return subcategoryRepository.findAll();
    }

    public List<Subcategory> findByCategoryId(Long id) {
        return subcategoryRepository.findByCategoryId(id);
    }

    public List<Products> getProductsBySubcategoryId(Long id) {
        return subcategoryRepository.findById(id).get().getProducts();
    }
}