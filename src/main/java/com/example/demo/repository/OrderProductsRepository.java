package com.example.demo.repository;

import com.example.demo.entity.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Long> {
    List<OrderProducts> findOrderProductsByCartId(Long id);
}