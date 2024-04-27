//package com.example.demo.controllers;
//
//import com.example.demo.entity.*;
//
//import com.example.demo.repository.ProductsRepository;
//import com.example.demo.repository.UsersRepository;
//import com.example.demo.service.CartService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/cart")
//@RequiredArgsConstructor
//public class CartController {
//    private final ProductsRepository productsRepository;
//    private final CartService cartService;
//    private final UsersRepository usersRepository;
//    private final HttpSession session;
//    private String JSESSIONID;
//
//
//    @GetMapping
//    public String showCart(Model model, HttpServletRequest request) {
////        if (JSESSIONID == null) {
////            JSESSIONID = session.getId();
////        }
//        String JSESSIONID = cookieInstall(request);
//        Users user = (Users) getCurrentUser();
//        if (user == null && cartService.findByName(JSESSIONID) != null) {
//            Cart cart = cartService.findByName(JSESSIONID);
//            model.addAttribute("cart", cart);
//            model.addAttribute("authentication", "ROLE_SESSIONUSER");
//            double totalPrice = calculateTotalPrice(cart);
//            model.addAttribute("totalPrice", totalPrice);
//            System.out.println(JSESSIONID);
//            return "cart";
//        } else if (user == null && cartService.findByName(JSESSIONID) == null) {
//            Cart cart = new Cart();
//            cart.setName(JSESSIONID);
//            cartService.save(cart);
//            model.addAttribute("cart", cart);
//            model.addAttribute("authentication", "ROLE_SESSIONUSER");
//            return "cart";
//        }
//
//        if (isUserAuthenticated()) {
//            if (user.getCart() == null) {
//                Users checkUsers = checkUserAndCart(JSESSIONID);
//                if (checkUsers.getCart() != null) {
//                    if (checkUsers.getCart().getProducts() != null) {
//                        List<Products> products = checkUsers.getCart().getProducts();
//                        model.addAttribute("cart", checkUsers.getCart());
//                        double totalPrice = calculateTotalPrice(checkUsers.getCart());
//                        model.addAttribute("authentication", user.getRole());
//                        model.addAttribute("totalPrice", totalPrice);
//                    } else {
//                        model.addAttribute("emptyCart", true);
//                    }
//                    return "cart";
//                }
//
//            }
//            //TODO  HERE SEEEEEEEEEEEE
//            Optional<Cart> cartOptional = cartService.findById(user.getCart().getId());
//            Cart cart = cartOptional.orElse(null);
//
//            //TODO LOGIC FIX HERE
//            if (user.getCart() != null && cartService.findByName(JSESSIONID) != null) {
//
//                Cart cartFromSession = cartService.findByName(JSESSIONID);
//
//                List<Products> products = cartFromSession.getProducts();
//                List<Products> productsFromDB = cart.getProducts();
//                for (Products product : products) {
//                    if (productsFromDB.contains(product)) {
//                        for (Products p : productsFromDB) {
//                            if (p.getId().equals(product.getId())) {
//                                p.setQuantity(p.getQuantity() + product.getQuantity());
//                                p.setMaxQuantity(p.getMaxQuantity() - product.getQuantity());
//                            }
//                        }
//                    } else {
//                        product.setMaxQuantity(product.getMaxQuantity() - product.getQuantity());
//                        productsFromDB.add(product);
//                    }
////                    Products productFromDB = productsRepository.findById(product.getId()).get();
////                    productFromDB.setMaxQuantity(productFromDB.getMaxQuantity() - product.getQuantity());
////                    productsRepository.save(productFromDB);
//                }
////                productsFromDB.addAll(products);
//                cart.setProducts(productsFromDB);
//                cartService.save(cart);
//                session.removeAttribute("cart");
//                model.addAttribute("cart", cart);
//                double totalPrice = calculateTotalPrice(cart);
//                model.addAttribute("authentication", user.getRole());
//                model.addAttribute("totalPrice", totalPrice);
//            } else if (user.getCart() != null && cartService.findByName(JSESSIONID) == null) {
//                cart = user.getCart();
//                model.addAttribute("cart", cart);
//                double totalPrice = calculateTotalPrice(cart);
//                model.addAttribute("authentication", user.getRole());
//                model.addAttribute("totalPrice", totalPrice);
//            } else {
//                model.addAttribute("emptyCart", true);
//            }
//            JSESSIONID = null;
//
//            System.out.println(JSESSIONID);
//            return "cart";
//        }
//        return "cart";
//    }
//
//    private String cookieInstall(HttpServletRequest request) {
//        String JSESSIONID = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("uuid")) {
//                    JSESSIONID = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        this.JSESSIONID = JSESSIONID;
//        return JSESSIONID;
//    }
//
//
//    @PostMapping("/add/{id}")
//    public String addToCart(@PathVariable Long id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
//        String JSESSIONID = cookieInstall(request);
//        String referer = request.getHeader("Referer");
//        Optional<Products> productOptional = productsRepository.findById(id);
//        if (!productOptional.isPresent()) {
//            return "redirect:/";
//        }
//        Products product = productOptional.get();
//        Users user = (Users) getCurrentUser();
//        Cart cart = null;
//
//        if (user == null && cartService.findByName(JSESSIONID) == null) {
//            cart = new Cart();
//            cart.setName(JSESSIONID);
//            Users usersSession = new Users();
//            usersSession.setEmail(JSESSIONID);
//            cart.setUsers(usersSession);
//            Order order = new Order();
//            order.setUsers(usersSession);
//            OrderProducts orderProduct = new OrderProducts();
//            orderProduct.setQuantity(quantity);
//            orderProduct.setProduct(product);
//            orderProduct.setOrder(order);
//            order.getOrderProducts().add(orderProduct); // Add OrderProducts to Order
//            usersRepository.save(usersSession);
//            cartService.save(cart);
//            usersSession.setCart(cart);
//            return "redirect:" + referer;
//        }
//
//        if (user == null && cartService.findByName(JSESSIONID) != null) {
//            cart = cartService.findByName(JSESSIONID);
//            addProductsToCard(quantity, cart, product, referer);
//            return "redirect:" + referer;
//        }
//
//        if (isUserAuthenticated()) {
//
//            if (user.getCart() != null) {
//                cart = cartService.findByName(user.getCart().getName());
//            }
//            if (cart != null) {
//                addProductsToCard(quantity, cart, product, referer);
//                return "redirect:" + referer;
//            } else {
//                cart = new Cart();
//                cart.setName(user.getEmail());
//                cart.setPhone(user.getPhone());
//                cart.setUsers(user);
//                usersRepository.save(user);
//                cartService.save(cart);
//                user.setCart(cart);
//                usersRepository.save(user);
//                addProductsToCard(quantity, cart, product, referer);
//            }
//        }
//
//        addProductsToCard(quantity, cart, product, referer);
//        return "redirect:" + referer;
//    }
//
//    private String addProductsToCard(int quantity, Cart cart, Products product, String referer) {
//        Order order = new Order();
//        order.setUsers(cart.getUsers());
//        OrderProducts orderProduct = new OrderProducts();
//        orderProduct.setQuantity(quantity);
//        orderProduct.setProduct(product);
//        orderProduct.setOrder(order);
//        order.getOrderProducts().add(orderProduct); // Add OrderProducts to Order
//        cartService.save(cart);
//        return "redirect:" + referer;
//    }
//
//    @PostMapping("/change/{id}")
//    public String changeQuantity(@PathVariable Long id, @RequestParam("quantity") int quantity) {
//        Optional<Products> productOptional = productsRepository.findById(id);
//        if (!productOptional.isPresent()) {
//            return "redirect:/";
//        }
//        Products product = productOptional.get();
//        Users user = (Users) getCurrentUser();
//
//        if (user == null && cartService.findByName(JSESSIONID) != null) {
//            Cart cart = cartService.findByName(JSESSIONID);
//            List<Products> productList = cart.getProducts();
//            if (productList == null) {
//                productList = new ArrayList<>();
//            }
//            counter(quantity, productList, product);
//            cart.setProducts(productList);
//            cartService.save(cart);
//            return "redirect:/cart";
//        }
//        if (user != null) {
//            Cart cart = cartService.findByName(user.getCart().getName());
//            List<Products> productList = cart.getProducts();
//            if (productList == null) {
//                productList = new ArrayList<>();
//            }
//            counter(quantity, productList, product);
//            cart.setProducts(productList);
//            cartService.save(cart);
//            return "redirect:/cart";
//        }
//        return "redirect:/cart";
//    }
//
//    @GetMapping("/add")
//    public String getProductToCart() {
//        return "redirect:/";
//    }
//
//    @PostMapping("/remove/{id}")
//    public String removeProductFromCart(@PathVariable Long id) {
//        Users user = (Users) getCurrentUser();
//        if (user == null && cartService.findByName(JSESSIONID) != null) {
//            Cart cart = cartService.findByName(JSESSIONID);
//            List<Products> products = cart.getProducts();
//            //перед удалением продукта из корзины, возвращаем его количество на склад
//            for (Products product : products) {
//                if (product.getId().equals(id)) {
//                    Products productFromDB = productsRepository.findById(id).get();
//                    productFromDB.setMaxQuantity(productFromDB.getMaxQuantity() + product.getQuantity());
//                    productsRepository.save(productFromDB);
//                }
//            }
//            products.removeIf(product -> product.getId().equals(id));
//            cart.setProducts(products);
//            cartService.save(cart);
//            return "redirect:/cart";
//        }
//        if (user != null) {
//            Long cartId = user.getCart().getId();
//            Cart cart = cartService.findById(cartId).get();
//            List<Products> products = cart.getProducts();
//            //перед удалением продукта из корзины, возвращаем его количество на склад
//            for (Products product : products) {
//                if (product.getId().equals(id)) {
//                    Products productFromDB = productsRepository.findById(id).get();
//                    productFromDB.setMaxQuantity(productFromDB.getMaxQuantity() + product.getQuantity());
//                    productsRepository.save(productFromDB);
//                }
//            }
//            products.removeIf(product -> product.getId().equals(id));
////            for (Products product : products) {
////                if (product.getId().equals(id)) {
////                    Products productFromDB = productsRepository.findById(id).get();
////                    productFromDB.setMaxQuantity(productFromDB.getMaxQuantity() + product.getQuantity());
////                    productsRepository.save(productFromDB);
////                }
////            }
//
//            cart.setProducts(products);
//            cartService.save(cart);
//
//            return "redirect:/cart";
//
//
//        }
//        return "redirect:/cart";
//    }
//
//    public boolean isUserAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.isAuthenticated();
//    }
//
//    public Users getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            String name = ((UserDetails) principal).getUsername();
//            return usersRepository.findUsersByEmail(name);
//        } else {
//            return null;
//        }
//    }
//
//    public double calculateTotalPrice(Cart cart) {
//        double totalPrice = 0.0;
//
//        for (Products product : cart.getProducts()) {
//            totalPrice += product.getPrice() * product.getQuantity();
//        }
//
//        return totalPrice;
//    }
//
//    public Users checkUserAndCart(String JSESSIONID) {
//        Users userCurrent = (Users) getCurrentUser();
//        Users userFromDB = usersRepository.findUsersByEmail(userCurrent.getEmail());
//        if (userFromDB.getCart() == null && cartService.findByName(JSESSIONID) != null) {
//            // here 1
//            Cart cartFromSession = cartService.findByName(JSESSIONID);
//            userFromDB.setCart(cartFromSession);
//            usersRepository.save(userFromDB);
//            return userFromDB;
//        } else if (userFromDB.getCart() != null && cartService.findByName(JSESSIONID) != null) {
//            Cart cartFromSession = cartService.findByName(JSESSIONID);
//            List<Products> products = cartFromSession.getProducts();
//            List<Products> productsFromDB = userFromDB.getCart().getProducts();
//            productsFromDB.addAll(products);
//            userFromDB.getCart().setProducts(productsFromDB);
//            usersRepository.save(userFromDB);
//            session.removeAttribute("cart");
//            return userFromDB;
//        } else if (userFromDB.getCart() == null && cartService.findByName(JSESSIONID) == null) {
//            Cart cart = new Cart();
//            cart.setName(userCurrent.getEmail());
//            cartService.save(cart);
//            userFromDB.setCart(cart);
//            usersRepository.save(userFromDB);
//            return userFromDB;
//        }
//        return userFromDB;
//    }
//
//    private static void counter(int quantity, List<Products> productList, Products product) {
//        if (productList.contains(product)) {
//            for (Products p : productList) {
//                if (p.getId().equals(product.getId())) {
//                    int currentQuantity = p.getQuantity();
//                    if (quantity > currentQuantity) {
//                        int difference = quantity - currentQuantity;
//                        if (p.getMaxQuantity() >= difference) {
//                            p.setMaxQuantity(p.getMaxQuantity() - difference);
//                            p.setQuantity(quantity);
//                        } else {
//                            // Недостаточно товара на складе
//                        }
//                    } else if (quantity < currentQuantity) {
//                        int difference = currentQuantity - quantity;
//                        p.setMaxQuantity(p.getMaxQuantity() + difference);
//                        p.setQuantity(quantity);
//                    }
//                }
//            }
//        }
//    }
//
//}