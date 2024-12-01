package com.example.demo.controllers;


import com.example.demo.entity.*;
import com.example.demo.repository.ProductsRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderProductsService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UsersService usersService;
    private final OrderProductsService orderProductsService;
    private final ProductsRepository productsRepository;
    private List<Order> orderForSave;

    //    @GetMapping("/")
//    @Transactional
//    public String showOrderForCurrentUser(Model model) {
//        Users users = getCurrentUser();
//        List<Order> orders = orderService.findAllOrdersByUsers(users.getEmail());
//        if (users.getCart()==null) {
//            return "orders";
//        }
//        List<OrderProducts> orderProducts = users.getCart().getOrderProducts();

    /// /        for (OrderProducts orderProduct : orderProducts) {
    /// /            for (Products product : orderProduct.getProducts()) {
    /// /                System.out.println(product.getName());
    /// /                System.out.println(orderProduct.getQuantity());
    /// /                System.out.println(product.getPrice());
    /// /            }
    /// /        }
//
//        System.out.println(users.getId());// Запрашиваем заказы пользователя из базы данных
//        // Запрашиваем заказы пользователя из базы данных
//        model.addAttribute("orders", orders);
//        model.addAttribute("orderProductsSeparate", orderProducts);
//        return "orders";
//    }
    @GetMapping("/admin")
    @Transactional
    public String showOrderForADMIN(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = false;
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_USER")) {
                    isAuthenticated = true;
                    break;
                } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    isAuthenticated = true;
                    break;
                } else {
                    isAuthenticated = false;
                }
            }
        }

        Collection<? extends GrantedAuthority> authoritiesForProductPageDeleteItems = authentication.getAuthorities();
        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("isAdmin", true);
                break;
            } else {
                model.addAttribute("isAdmin", false);
            }
        }


        //TODO LOOK AT HERE ALL ORDERS FOR ADMIN
        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                List<Order> orderProductsForAdmin = orderService.getAllOrders();
                model.addAttribute("isAdmin", true);
                model.addAttribute("orderProductsForAdmin", orderProductsForAdmin);
            }
        }
        return "all-users-orders";

    }

    @GetMapping("/")
    @Transactional
    public String showOrderForCurrentUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = false;
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_USER")) {
                    isAuthenticated = true;
                    break;
                } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    isAuthenticated = true;
                    break;
                } else {
                    isAuthenticated = false;
                }
            }
        }

        Collection<? extends GrantedAuthority> authoritiesForProductPageDeleteItems = authentication.getAuthorities();
        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("isAdmin", true);
                break;
            } else {
                model.addAttribute("isAdmin", false);
            }
        }
        model.addAttribute("isAuthenticated", isAuthenticated);
        Users users = getCurrentUser();
        if (users == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.findAllOrdersByUsers(users.getEmail());
        model.addAttribute("orders", orders);

//        if (users.getCart() != null) {
//            List<OrderProducts> orderProducts = users.getCart().getOrderProducts();
//            model.addAttribute("orderProductsSeparate", orderProducts);
//        }
//        //TODO LOOK AT HERE ALL ORDERS FOR ADMIN
//        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
//            if (authority.getAuthority().equals("ROLE_ADMIN")) {
//                List<Order> orderProductsForAdmin = orderService.getAllOrders();
//                model.addAttribute("isAdmin", true);
//                model.addAttribute("orderProductsForAdmin", orderProductsForAdmin);
//            } else if (users.getCart() != null) {
//                List<OrderProducts> orderProducts = users.getCart().getOrderProducts();
//                model.addAttribute("orderProductsSeparate", orderProducts);
//            }
//        }

        return "orders";
    }

    // TODO CHANGES HERE
    @PostMapping("/find")
    public String findOrdersByEmail(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "name", required = false) String name, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authoritiesForProductPageDeleteItems = authentication.getAuthorities();
        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("isAdmin", true);
                List<Order> ordersFounded = orderService.findAllByEmailContain(email, name);
                model.addAttribute("ordersFounded", ordersFounded);
                break;
            } else {
                model.addAttribute("isAdmin", false);
            }
        }
        return "found_orders";
    }

//    @PostMapping("/find")
//    public String findOrdersByEmail(@RequestParam(value = "email", required = false) String email,
//                                    @RequestParam(value = "name", required = false) String name,
//                                    Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//        model.addAttribute("isAdmin", isAdmin);
//
//        List<Order> ordersFounded = new ArrayList<>();
//        if (isAdmin) {
//            if (email != null && !email.isEmpty() && name != null && !name.isEmpty()) {
//                ordersFounded = orderService.findAllByBuyerEmailContainingAndNameContaining(email, name);
//            } else if (email != null && !email.isEmpty()) {
//                ordersFounded = orderService.findAllByBuyerEmailContaining(email);
//            } else if (name != null && !name.isEmpty()) {
//                ordersFounded = orderService.findAllByNameContaining(name);
//            } else {
////                ordersFounded = orderService.findAll(); // Опционально, если нужно вернуть все заказы
//            }
//        }
//
//        model.addAttribute("ordersFounded", ordersFounded);
//        return "found_orders";
//    }

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

    /// /        cartService.delete(cart);
//        return "orders";
//    }
    @PostMapping("/{id}")
    @Transactional
    public String showOrders(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = false;
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_USER")) {
                    isAuthenticated = true;
                    break;
                } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    isAuthenticated = true;
                    break;
                } else {
                    isAuthenticated = false;
                }
            }
        }

        Collection<? extends GrantedAuthority> authoritiesForProductPageDeleteItems = authentication.getAuthorities();
        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("isAdmin", true);
                break;
            } else {
                model.addAttribute("isAdmin", false);
            }
        }
        model.addAttribute("isAuthenticated", isAuthenticated);

        Users userAuth = getCurrentUser();
        if (userAuth == null) {
            return "redirect:/login";
        }
        if (userAuth.getCart().getOrderProducts().isEmpty()) {
            return "redirect:/products/list";
        }
        // TODO ЛИШНЕЕ Get the cart by id
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

        //TODO SUPER PUPER CRITICAL Clear the cart
        userAuth.getCart().getOrderProducts().clear();
        cartService.save(userAuth.getCart());
        List<OrderProducts> orderProductsFromDataBase = orderProductsService.findByCartId(cart.getId());
        orderProductsFromDataBase.forEach(orderProducts ->
        {
            orderProducts.setCart(null);
//                    orderProducts.setOrder(order);
            orderProductsService.save(orderProducts);
        });

        System.out.println(userAuth.getCart().getId());
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

    /// /        orderService.getOrders(users);
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