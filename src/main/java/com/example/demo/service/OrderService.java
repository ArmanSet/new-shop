package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

//    public void saveOrder(Cart cart, Users users, double totalPrice) {
//        Order order = new Order();
//        order.setBuyerEmail(users.getEmail());
//        order.setBuyerName(users.getName());
//        order.setBuyerPhone(users.getPhone());
//        order.setBuyerAddress(users.getAddress());
////        order.setOrderAmount(new BigDecimal(cart.getTotalPrice()));
//        order.setOrderStatus(0);
////        order.setProducts(cart.getProducts());
//        List<Products> productsForOrder = new ArrayList<>(cart.getProducts());
//        OrderProducts orderProducts = new OrderProducts();
//        List<OrderProducts> orderProductsList = new ArrayList<>();
//
//        for (Products product : productsForOrder) {
//            orderProducts.setProduct(product);
//            orderProducts.setOrder(order);
//            orderProducts.setQuantity(product.getQuantity());
//        }
//        orderProductsList.add(orderProducts);
//        order.setOrderProducts(orderProductsList);
//        order.setCreateTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
//        order.setOrderAmount(new BigDecimal(totalPrice));
//        order.setUsers(users);
//        order.setDescription("Order from " + users.getName() + " at " + order.getCreateTime());
//        orderRepository.save(order);
//        cartService.clearCart(cart);
//
//
//    }

    public List<Order> getOrders(String usersEmail) {
        List<Order> orders = orderRepository.findAllByBuyerEmail(usersEmail);
        for (Order order : orders) {
            System.out.println(order.getBuyerEmail());
            System.out.println(order.getBuyerName());
            System.out.println(order.getBuyerPhone());
            System.out.println(order.getBuyerAddress());
            System.out.println(order.getOrderAmount());
            System.out.println(order.getCreateTime());
            System.out.println(order.getDescription());
            System.out.println(order.getOrderStatus());
            for (OrderProducts orderProducts : order.getOrderProducts()) {
//                System.out.println(orderProducts.getProduct().getName());
                System.out.println(orderProducts.getQuantity());
            }
        }
        return orders;
    }
//
//    public double calculateTotalPrice(Cart cart) {
//        return cart.getProducts().stream()
//                .mapToDouble(product -> (product.getPrice() * product.getQuantity()))
//                .sum();
//    }


    public List<Order> findByUserEmail(String name) {
        return orderRepository.findAllByBuyerEmail(name);
    }
}