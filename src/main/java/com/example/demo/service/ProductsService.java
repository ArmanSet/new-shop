package com.example.demo.service;

import com.example.demo.entity.Products;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Products> findAll() {
       return productsRepository.findAll();
    }

    public void save(Products products) {
        productsRepository.save(products);
    }

    public void deleteById(Long id) {
        productsRepository.deleteById(id);
    }

    public List<Products> findByCategoryId(Long categoryId) {
       return productsRepository.findByCategoryId(categoryId);
    }

    public List<Products> findByCategory(Long categoryId) {
        return productsRepository.findByCategoryId(categoryId);
    }

    public List<Products> findBySubcategoryId(Long id) {
        return productsRepository.findBySubcategoryId(id);
    }
}