package com.example.demo.controllers;


import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UsersService usersService;

@GetMapping("/")
public String showOrderForCurrentUser(Model model) {
    Users users = getCurrentUser();
    List<Order> orders = orderService.getOrders(users.getEmail());
    model.addAttribute("orders", orders);
    return "orders";
}

    @PostMapping("/{id}")
    public String showOrders(@PathVariable Long id, Model model) {
        Users userAuth = getCurrentUser();
        if (userAuth == null) {
            return "redirect:/login";
        }
        Cart cart = cartService.findById(id).get();
        Users users = cart.getUsers();
        System.out.println(users.getEmail());
        double totalPrice = orderService.calculateTotalPrice(cart);
        orderService.saveOrder(cart, users, totalPrice);
        List<Order> orders = orderService.getOrders(users.getEmail());
        System.out.println("=====================================");
        System.out.println(orders.get(0).getOrderAmount());

        model.addAttribute("orders", orders);
        model.addAttribute("totalPrice", totalPrice);

//      Users users = getCurrentUser();


//        orderService.getOrders(users);

        return "orders";
    }
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