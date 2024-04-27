package com.example.demo.controllers;


import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderProductsService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController2 {
    private final UsersRepository usersRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderProductsService orderProductsService;
    private final ProductsRepository productRepository;
    private final HttpSession session;
    private final CartRepository cartRepository;
    private final CartService cartService;


    private String JSESSIONID;

    @GetMapping
    public String showCart(Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        //TODO USER Аутентифицирован
        if (isUserAuthenticated()) {
            Users user = getCurrentUser();
            if (cartService.checkInCockieIfCartExist(request) != null && user.getCart().getOrderProducts().getFirst()!=null) {
                 cartService.mergeCarts(user, request);

                 return "cart" ;
            } else if (cartService.checkInCockieIfCartExist(request) != null && user.getCart().getOrderProducts().getFirst()==null) {
               cartService.mergeCarts(user, request);
               return "cart";
            }
            if (user.getCart() == null) {
                Cart cart = new Cart();
                cart.setEmail(user.getEmail());
                cart.setAddress(user.getAddress());
                cart.setName(user.getName());
                cart.setPhone(user.getPhone());
                cart.setUsers(user);
                user.setCart(cart);
                usersRepository.save(user);
                cartRepository.save(cart);
                model.addAttribute("cart", cart);
                model.addAttribute("orderProducts", cart.getOrderProducts());
                model.addAttribute("authentication", user.getRole());
                model.addAttribute("totalPrice", 0);
                return "cart";
            } else {
                Cart cart = user.getCart();
                System.out.println(cart.getOrderProducts().getFirst().getProducts().getFirst().getName());
                cart = cartService.convertProductsFromManyOrderProductsToOneForShow(cart);
                model.addAttribute("cart", cart);
                model.addAttribute("orderProducts", cart.getOrderProducts());
                model.addAttribute("authentication", user.getRole());
                double totalPrice = cartService.calculateTotalPrice(cart);
                model.addAttribute("totalPrice", totalPrice);
                cartService.save(cart);
                return "cart";

            }
        }
        //TODO USER НЕ Аутентифицирован
        if (!isUserAuthenticated()){
            String uuid = cartService.getCookieValue(request, "uuid");
            if (cartService.findCartByName(uuid) == null) {
                Cart cart = new Cart();
                cart.setName(uuid);
                cartRepository.save(cart);
                model.addAttribute("cart", cart);
                model.addAttribute("orderProducts", cart.getOrderProducts());
                model.addAttribute("authentication", "GUEST");
                model.addAttribute("totalPrice", 0);
                return "cart";
            }
            else {
                Cart cart = cartService.findCartByName(uuid);
                cart = cartService.convertProductsFromManyOrderProductsToOneForShow(cart);
                model.addAttribute("cart", cart);
                model.addAttribute("orderProducts", cart.getOrderProducts());
                model.addAttribute("authentication", "GUEST");
                double totalPrice = cartService.calculateTotalPrice(cart);
                model.addAttribute("totalPrice", totalPrice);
                cartService.save(cart);
                return "cart";
            }
        }

        return "cart";
    }


    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable Long id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (isUserAuthenticated()) {
            Users user = getCurrentUser();
            OrderProducts orderProducts = new OrderProducts();
            List<Products> listProducts = new ArrayList<>();
            listProducts.add(productRepository.findById(id).get());
            orderProducts.setProducts(listProducts);
            orderProducts.setQuantity(quantity);
            if (user.getCart() == null) {
                Cart cart = new Cart();
                cart.setEmail(user.getEmail());
                cart.setAddress(user.getAddress());
                cart.setName(user.getName());
                cart.setPhone(user.getPhone());
                cart.setUsers(user);
                List<OrderProducts> orderProductsList = new ArrayList<>();
                orderProductsList.add(orderProducts);
                cart.setOrderProducts(orderProductsList);
                user.setCart(cart);
                cartService.save(cart);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
                usersRepository.save(user);
                //return "redirect:" + referer;
            } else {
                Cart cart = user.getCart();
                cart.getOrderProducts().add(orderProducts);
                cartRepository.save(cart);
                user.setCart(cart);
                usersRepository.save(user);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
                //return "redirect:" + referer;
            }


            return "redirect:" + referer;
        }
        if (!isUserAuthenticated()) {
            String uuid = cartService.getCookieValue(request, "uuid");
            OrderProducts orderProducts = new OrderProducts();
            List<Products> listProducts = new ArrayList<>();
            listProducts.add(productRepository.findById(id).get());
            orderProducts.setProducts(listProducts);
            orderProducts.setQuantity(quantity);
            if (cartService.findCartByName(uuid) == null) {
                Cart cart = new Cart();
                cart.setName(uuid);
                List<OrderProducts> orderProductsList = new ArrayList<>();
                orderProductsList.add(orderProducts);
                cart.setOrderProducts(orderProductsList);
                cartRepository.save(cart);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
                //return "redirect:" + referer;
            } else {
                Cart cart = cartService.findCartByName(uuid);
                cart.getOrderProducts().add(orderProducts);
                cartRepository.save(cart);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
                //return "redirect:" + referer;
            }
            return "redirect:" + referer;
        }
        return "redirect:/cart/show";
    }


    //    public String addProductToCart(@PathVariable Long id, HttpServletRequest request) {
//        Users user = getCurrentUser();
//        if (isUserAuthenticated()) {
//            if (user.getCart() == null) {
//                Cart cart = new Cart();
//                user.setCart(cart);
//                usersRepository.save(user);
//            }
//            Cart cart = user.getCart();
//            cart.addProduct(productRepository.findById(id).get());
//            usersRepository.save(user);
//        } else {
//            cookieInstall(request);
//            Cart cart = (Cart) session.getAttribute(JSESSIONID);
//            if (cart == null) {
//                cart = new Cart();
//                session.setAttribute(JSESSIONID, cart);
//            }
//            cart.addProduct(productRepository.findById(id).get());
//        }
//        return "redirect:/cart/show";
//    }


    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String name = ((UserDetails) principal).getUsername();
            return usersRepository.findUsersByEmail(name);
        } else {
            return null;
        }
    }

    public boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails;
    }

    private String cookieInstall(HttpServletRequest request) {
        String JSESSIONID = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("uuid")) {
                    JSESSIONID = cookie.getValue();
                    break;
                }
            }
        }
        this.JSESSIONID = JSESSIONID;
        return JSESSIONID;
    }



    @PostMapping("/change/{id}")
    public String changeQuantity(@PathVariable Long id, @RequestParam int quantity) {
        Optional<OrderProducts> orderProductOptional = orderProductsService.findById(id);
        if (orderProductOptional.isPresent()) {
            OrderProducts orderProduct = orderProductOptional.get();
            orderProduct.setQuantity(quantity);
            orderProductsService.save(orderProduct);
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeProduct(@PathVariable Long id) {
        orderProductsService.deleteById(id);
        return "redirect:/cart";
    }
}

