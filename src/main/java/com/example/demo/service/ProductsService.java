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

    public Products findProductById(Long id) {
       return productsRepository.findById(id).get();
    }

    public List<Products> findAllByNameContaining(String name) {
        return productsRepository.findAllByNameContainingIgnoreCase(name);
    }

    public void disableProduct(Long id) {
        Products product = productsRepository.findById(id).get();
        product.setActive(0);
        productsRepository.save(product);
    }

    public void enableProduct(Long id) {
        Products product = productsRepository.findById(id).get();
        product.setActive(1);
        productsRepository.save(product);
    }


}