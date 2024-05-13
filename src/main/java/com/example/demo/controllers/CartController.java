package com.example.demo.controllers;


import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderProductsService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
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
    public String showCart(Model model, HttpServletRequest request, HttpServletResponse responce) {
        String referer = request.getHeader("Referer");

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

        System.out.println(isAuthenticated);
        model.addAttribute("isAuthenticated", isAuthenticated);
        //TODO USER Аутентифицирован
        if (isUserAuthenticated()) {
            Users users = getCurrentUser();
            Users user = usersRepository.findUsersByEmail(users.getEmail());
            if (user.getCart() == null) {
                cartService.createCartForUser(user);
                if (cartService.checkInCockieIfCartExist(request) != null) {
                    Cart cart = cartService.mergeCarts(user, request);
                    System.out.println(cart.getId());
                    cartService.save(cart); // <-- Важно не забыть сохранить корзину после изменений
                    return showPage(model, request, responce, cart, user);
                }
                model.addAttribute("cart", user.getCart());
                model.addAttribute("orderProducts", user.getCart().getOrderProducts());
                model.addAttribute("authentication", user.getRole());
                model.addAttribute("totalPrice", 0);
                return "redirect:/cart";
            } else {
                Cart cart = user.getCart();
//                String uuid = cartService.getCookieValue(request, "uuid");
                //TODO ПРОБЛЕМЫ У 2 ЮЗЕРА ТУТ ПАДАЕТ С ОШИБКОЙ
                if (cartService.checkInCockieIfCartExist(request) != null) {
                    cart = cartService.mergeCarts(user, request);
                    cartService.save(cart); // <-- Важно не забыть сохранить корзину после изменений
                    return showPage(model, request, responce, cart, user);
                }

                return showPage(model, request, responce, cart, user);

            }
        }
        //TODO USER НЕ Аутентифицирован
        if (!isUserAuthenticated()) {
            String uuid = cartService.getCookieValue(request, "uuid");
            if (cartService.findCartByName(uuid) == null) {
//                Cart cart = new Cart();
//                cart.setName(uuid);
//                cartRepository.save(cart);
//                model.addAttribute("cart", cart);
//                model.addAttribute("orderProducts", cart.getOrderProducts());
                model.addAttribute("authentication", "ROLE_ANONYMOUS");
//                model.addAttribute("totalPrice", 0);
                return "cart";
            } else {
                //TODO SUPER CRITICAL
                Cart cart = cartService.findCartByName(uuid);
                Long id = cart.getId();
                List<OrderProducts> orderProductsByCartId = orderProductsService.findOrderProductsByCartId(id);
                cart = cartService.convertProductsFromManyOrderProductsToOneForShow(cart);

                model.addAttribute("cart", cart);
                model.addAttribute("orderProducts", orderProductsByCartId);
                model.addAttribute("authentication", "GUEST");
                double totalPrice = cartService.calculateTotalPrice(cart);
                model.addAttribute("totalPrice", totalPrice);
//                cartService.save(cart);
                return "cart";
            }
        }

        return "cart";
    }

    private String showPage(Model model, HttpServletRequest request, HttpServletResponse responce, Cart cart, Users user) {
        model.addAttribute("cart", cart);
        model.addAttribute("orderProducts", cart.getOrderProducts());
        model.addAttribute("authentication", user.getRole());
        double totalPrice = cartService.calculateTotalPrice(cart);
        model.addAttribute("totalPrice", totalPrice);
        cartService.save(cart);
//        cartService.clearCookie(request, responce,"uuid");
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable Long id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        Products product = productRepository.findById(id).get();
        if (product.getMaxQuantity() < quantity) {
            // Here you might want to add some error message indicating that there is not enough product in stock
            return "redirect:/cart";
        }
        product.setMaxQuantity(product.getMaxQuantity() - quantity);
        productRepository.save(product);

        if (isUserAuthenticated()) {
            Users user = getCurrentUser();
            OrderProducts orderProducts = new OrderProducts();
            List<Products> listProducts = new ArrayList<>();
            listProducts.add(product);
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
            } else {
                Cart cart = user.getCart();
                cart.getOrderProducts().add(orderProducts);
                cartRepository.save(cart);
                user.setCart(cart);
                usersRepository.save(user);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
            }
        } else {
            String uuid = cartService.getCookieValue(request, "uuid");
            OrderProducts orderProducts = new OrderProducts();
            List<Products> listProducts = new ArrayList<>();
            listProducts.add(product);
            orderProducts.setProducts(listProducts);
            orderProducts.setQuantity(quantity);
            List<OrderProducts> orderProductsListForGuest = new ArrayList<>();
            orderProductsListForGuest.add(orderProducts);
            Optional<Cart> cartForGuest = Optional.ofNullable(cartService.findCartByName((uuid)));
            if (cartForGuest.isEmpty()) {
                cartService.createCartForGuest(uuid, orderProductsListForGuest);
            }
            if (cartService.findCartByName(uuid) == null) {
                Cart cart = new Cart();
                cart.setName(uuid);
                cart.setEmail(uuid);
                List<OrderProducts> orderProductsList = new ArrayList<>();
                orderProductsList.add(orderProducts);
                cart.setOrderProducts(orderProductsList);
                cartRepository.save(cart);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
            } else {
                Cart cart = cartService.findCartByName(uuid);
                cart.getOrderProducts().add(orderProducts);
                cartRepository.save(cart);
                orderProducts.setCart(cart);
                orderProductsService.save(orderProducts);
            }
        }
        return "redirect:" + referer;
    }
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
    public String changeQuantity(@PathVariable Long id, @RequestParam int quantity, HttpServletRequest request) {
        Users user = getCurrentUser();
        if (isUserAuthenticated()) {
            Cart cart = user.getCart();
            List<OrderProducts> orderProducts = cart.getOrderProducts();
            for (OrderProducts op : orderProducts) {
                if (op.getId().equals(id)) {
                    List<Products> products = op.getProducts();
                    for (Products p : products) {
                        int difference = op.getQuantity() - quantity;
                        if (difference >= 0) {
                            p.setMaxQuantity(p.getMaxQuantity() + difference);
                        } else {
                            if (p.getMaxQuantity() >= Math.abs(difference)) {
                                p.setMaxQuantity(p.getMaxQuantity() + difference);
                            } else {
                                // Here you might want to add some error message indicating that there is not enough product in stock
                                return "redirect:/cart";
                            }
                        }
                        productRepository.save(p);
                        op.setQuantity(quantity);
                        orderProductsService.save(op);
                    }
                }
            }
            cart.setOrderProducts(orderProducts);
            cartRepository.save(cart);
        } else {
            String uuid = cartService.getCookieValue(request, "uuid");
            //TODO SUPER CRITICAL
            Cart cart = cartService.findCartByName(uuid);
            if (cart == null) {
                // Create a new Cart object if it doesn't exist
                cart = new Cart();
                cart.setName(uuid);
                cartRepository.save(cart);
            }
            List<OrderProducts> orderProducts = cart.getOrderProducts();
            for (OrderProducts op : orderProducts) {
                if (op.getId().equals(id)) {
                    List<Products> products = op.getProducts();
                    for (Products p : products) {
                        int difference = op.getQuantity() - quantity;
                        if (difference >= 0) {
                            p.setMaxQuantity(p.getMaxQuantity() + difference);
                        } else {
                            if (p.getMaxQuantity() >= Math.abs(difference)) {
                                p.setMaxQuantity(p.getMaxQuantity() + difference);
                            } else {
                                // Here you might want to add some error message indicating that there is not enough product in stock
                                return "redirect:/cart";
                            }
                        }
                        productRepository.save(p);
                        op.setQuantity(quantity);
                        orderProductsService.save(op);
                    }
                }
            }
            cart.setOrderProducts(orderProducts);
            cartRepository.save(cart);
        }

        return "redirect:/cart";
    }

// TODO Переделать метод CRITICAL Завтра
//    @PostMapping("/remove/{id}")
//    public String removeProduct(@PathVariable Long id, HttpServletRequest request) {
//        if (isUserAuthenticated()) {
//            Users user = getCurrentUser();
//            Cart cart = user.getCart();
//            List<OrderProducts> orderProducts = new ArrayList<>(cart.getOrderProducts());
//            List<OrderProducts> copyOrderProducts = new ArrayList<>(orderProducts);
//            for (OrderProducts op : copyOrderProducts) {
//                op.getProducts().removeIf(product -> product.getId().equals(id));
//                if (op.getProducts().isEmpty()) {
//                    orderProducts.remove(op);
//                    orderProductsService.delete(op); // delete from the database
//                }
//            }
//            cart.setOrderProducts(orderProducts);
//            cartRepository.save(cart);
//        } else {
//            String uuid = cartService.getCookieValue(request, "uuid");
//            Cart cart = cartService.findCartByName(uuid);
//            if (cart != null) {
//                List<OrderProducts> orderProducts = new ArrayList<>(cart.getOrderProducts());
//                List<OrderProducts> copyOrderProducts = new ArrayList<>(orderProducts);
//                for (OrderProducts op : copyOrderProducts) {
//                    op.getProducts().removeIf(product -> product.getId().equals(id));
//                    if (op.getProducts().isEmpty()) {
//                        orderProducts.remove(op);
//                        orderProductsService.delete(op); // delete from the database
//                    }
//                }
//                cart.setOrderProducts(orderProducts);
//                cartRepository.save(cart);
//            }
//        }
//        return "redirect:/cart";
//    }

    @PostMapping("/remove/{id}")
    public String removeProduct(@PathVariable Long id, HttpServletRequest request) {
        if (isUserAuthenticated()) {
            Users user = getCurrentUser();
            Cart cart = user.getCart();
            List<OrderProducts> orderProducts = new ArrayList<>(cart.getOrderProducts());
            List<OrderProducts> copyOrderProducts = new ArrayList<>(orderProducts);
            for (OrderProducts op : copyOrderProducts) {
                List<Products> products = op.getProducts();
                for (Products p : products) {
                    if (p.getId().equals(id)) {
                        p.setMaxQuantity(p.getMaxQuantity() + op.getQuantity()); // Increase maxQuantity
                        productRepository.save(p);
                        products.remove(p);
                        break;
                    }
                }
                if (products.isEmpty()) {
                    orderProducts.remove(op);
                    orderProductsService.delete(op); // delete from the database
                }
            }
            cart.setOrderProducts(orderProducts);
            cartRepository.save(cart);
        } else {
            String uuid = cartService.getCookieValue(request, "uuid");
            Cart cart = cartService.findCartByName(uuid);
            if (cart != null) {
                List<OrderProducts> orderProducts = new ArrayList<>(cart.getOrderProducts());
                List<OrderProducts> copyOrderProducts = new ArrayList<>(orderProducts);
                for (OrderProducts op : copyOrderProducts) {
                    List<Products> products = op.getProducts();
                    for (Products p : products) {
                        if (p.getId().equals(id)) {
                            p.setMaxQuantity(p.getMaxQuantity() + op.getQuantity()); // Increase maxQuantity
                            productRepository.save(p);
                            products.remove(p);
                            break;
                        }
                    }
                    if (products.isEmpty()) {
                        orderProducts.remove(op);
                        orderProductsService.delete(op); // delete from the database
                    }
                }
                cart.setOrderProducts(orderProducts);
                cartRepository.save(cart);
            }
        }
        return "redirect:/cart";
    }
}

