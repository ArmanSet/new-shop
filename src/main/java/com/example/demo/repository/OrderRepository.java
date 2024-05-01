package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.Products;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByBuyerEmail(String email);

    List<Order> findAllByUsersEmail(String email);
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderProducts op JOIN FETCH op.products WHERE o.buyerEmail = :email")
    List<Order> findAllByUsersEmailWithProducts(@Param("email") String email);

}
