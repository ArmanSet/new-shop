package com.example.demo.controllers;


import com.example.demo.entity.*;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderProductsService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UsersService usersService;
    private final OrderProductsService orderProductsService;
    private List<Order> orderForSave;

    @GetMapping("/")
    @Transactional
    public String showOrderForCurrentUser(Model model) {
        Users users = getCurrentUser();
        List<Order> orders = orderService.findAllOrdersByUsers(users.getEmail());
        List<OrderProducts> orderProducts = users.getCart().getOrderProducts();
        for (OrderProducts orderProduct : orderProducts) {
           for (Products product : orderProduct.getProducts()) {
               System.out.println(product.getName());
               System.out.println(orderProduct.getQuantity());
               System.out.println(product.getPrice());
           }
        }

        System.out.println(users.getId());// Запрашиваем заказы пользователя из базы данных
         // Запрашиваем заказы пользователя из базы данных
        model.addAttribute("orders", orders);
        model.addAttribute("orderProductsSeparate", orderProducts);
        return "orders";
    }

//    @PostMapping("/{id}")
//    public String showOrders(@PathVariable Long id, Model model) {
//        Users userAuth = getCurrentUser();
//        if (userAuth == null) {
//            return "redirect:/login";
//        }
//        Cart cart = cartService.findById(id).get();
//        Order order = new Order();
//        List<OrderProducts> clonedOrderProducts = new ArrayList<>(cart.getOrderProducts());
//        order.setOrderProducts(clonedOrderProducts);
//        order.setUsers(userAuth);
//
//        order.setUpdateTime(order.getUpdateTime());
//        order.setCreateTime(LocalDateTime.now());
//        order.setName(cart.getName());
//        order.setBuyerPhone(userAuth.getPhone());
//        order.setBuyerAddress(userAuth.getAddress());
//        order.setBuyerEmail(userAuth.getEmail());
//        order.setBuyerName(userAuth.getName());
//        StringBuilder combinedDescription = new StringBuilder();
//        for (OrderProducts orderProduct : order.getOrderProducts()) {
//            for (Products prod : orderProduct.getProducts()) {
//                combinedDescription.append(prod.getDescription());
//                combinedDescription.append(" "); //optional, adds a space between each description
//            }
//        }
//
//        order.setDescription(combinedDescription.toString());
//        order.setOrderAmount(orderService.calculateTotalPrice(cart));
//
//        orderService.save(order);    // Сохраняем заказ в базе данных
//
//        List<Order> orders = orderService.findAllOrdersByUsers(userAuth.getEmail()); // Запрашиваем обновленный список заказов пользователя из базы данных
//        model.addAttribute("orders", orders);
//        userAuth.setOrders(orders);
////        cartService.delete(cart);
//        return "orders";
//    }

    @PostMapping("/{id}")
    public String showOrders(@PathVariable Long id, Model model) {
        Users userAuth = getCurrentUser();
        if (userAuth == null) {
            return "redirect:/login";
        }
        Cart cart = cartService.findById(id).get();
        Order order = new Order();
        order.setUsers(userAuth);

        order.setCreateTime(LocalDateTime.now());
        order.setName(cart.getName());
        order.setBuyerPhone(userAuth.getPhone());
        order.setBuyerAddress(userAuth.getAddress());
        order.setBuyerEmail(userAuth.getEmail());
        order.setBuyerName(userAuth.getName());
        StringBuilder combinedDescription = new StringBuilder();

        order.setDescription(combinedDescription.toString());
        order.setOrderAmount(orderService.calculateTotalPrice(cart));

        orderService.save(order);    // Save the order first

        List<OrderProducts> clonedOrderProducts = new ArrayList<>();
        for (OrderProducts orderProduct : cart.getOrderProducts()) {
            OrderProducts newOrderProduct = new OrderProducts();
            newOrderProduct.setProducts(new ArrayList<>(orderProduct.getProducts())); // Create a new collection of products
            newOrderProduct.setQuantity(orderProduct.getQuantity()); // Set the quantity
            newOrderProduct.setOrder(order); // Set the order in the new OrderProducts
            orderProductsService.save(newOrderProduct); // Save the new OrderProducts
            clonedOrderProducts.add(newOrderProduct);
        }
        order.setOrderProducts(clonedOrderProducts);

        orderService.save(order);    // Save the order again after setting OrderProducts

        List<Order> orders = orderService.findAllOrdersByUsers(userAuth.getEmail()); // Get the updated list of orders for the user
        model.addAttribute("orders", orders);
        userAuth.setOrders(orders);

        cartService.delete(cart);
        return "orders";
    }

    //TODO BEST REALIZATION
//@PostMapping("/{id}")
//public String showOrders(@PathVariable Long id, Model model) {
//    Users userAuth = getCurrentUser();
//    if (userAuth == null) {
//        return "redirect:/login";
//    }
//    Cart cart = cartService.findById(id).get();
//    Order order = new Order();
//    order.setUsers(userAuth);
//
//    order.setCreateTime(LocalDateTime.now());
//    order.setName(cart.getName());
//    order.setBuyerPhone(userAuth.getPhone());
//    order.setBuyerAddress(userAuth.getAddress());
//    order.setBuyerEmail(userAuth.getEmail());
//    order.setBuyerName(userAuth.getName());
//    StringBuilder combinedDescription = new StringBuilder();
//
//    order.setDescription(combinedDescription.toString());
//    order.setOrderAmount(orderService.calculateTotalPrice(cart));
//
//    orderService.save(order);    // Save the order first
//
//    List<OrderProducts> clonedOrderProducts = new ArrayList<>(cart.getOrderProducts());
//    for (OrderProducts orderProduct : clonedOrderProducts) {
//        orderProduct.setOrder(order); // Set the order in the OrderProducts
//        orderProductsService.save(orderProduct); // Save the OrderProducts
//    }
//    order.setOrderProducts(clonedOrderProducts);
//
//    List<Order> orders = orderService.findAllOrdersByUsers(userAuth.getEmail()); // Get the updated list of orders for the user
//    model.addAttribute("orders", orders);
//    userAuth.setOrders(orders);
//    return "orders";
//}

//    @PostMapping("/{id}")
//    public String showOrders(@PathVariable Long id, Model model) {
//        Users userAuth = getCurrentUser();
//        if (userAuth == null) {
//            return "redirect:/login";
//        }
//        Cart cart = cartService.findById(id).get();
//        Users users = cart.getUsers();
//        System.out.println(users.getEmail());
////        double totalPrice = orderService.calculateTotalPrice(cart);
////        orderService.saveOrder(cart, users, totalPrice);
//        List<Order> orders = orderService.getOrders(users.getEmail());
//        System.out.println("=====================================");
//        System.out.println(orders.get(0).getOrderAmount());
//
//        model.addAttribute("orders", orders);
////        model.addAttribute("totalPrice", totalPrice);
//
////      Users users = getCurrentUser();
//
//
////        orderService.getOrders(users);
//
//        return "orders";
//    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String name = ((UserDetails) principal).getUsername();
            return usersService.findUsersByEmail(name);
        } else {
            return null;
        }
    }


}