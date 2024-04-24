package com.example.demo.controllers;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;

import com.example.demo.repository.ProductsRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ProductsRepository productsRepository;
    private CartService cartService;
    private UsersRepository usersRepository;
    private HttpSession session;
    private static String JSESSIONID = null;


    @Autowired
    public CartController(HttpSession session, CartService cartService, ProductsRepository productsRepository, UsersRepository usersRepository) {
        this.productsRepository = productsRepository;
        this.cartService = cartService;
        this.usersRepository = usersRepository;
        this.session = session;
    }


    @GetMapping
    public String showCart(Model model) {
        if (JSESSIONID == null) {
            JSESSIONID = session.getId();
        }


        Users user = (Users) getCurrentUser();
        if (user == null && cartService.findByName(session.getId()) != null) {
            Cart cart = cartService.findByName(session.getId());
            model.addAttribute("cart", cart);
            model.addAttribute("authentication", "ROLE_SESSIONUSER");
            double totalPrice = calculateTotalPrice(cart);
            model.addAttribute("totalPrice", totalPrice);
            System.out.println(session.getId());
            return "cart";
        } else if (user == null && cartService.findByName(session.getId()) == null) {
            Cart cart = new Cart();
            cart.setName(session.getId());
            cartService.save(cart);
            model.addAttribute("cart", cart);
            model.addAttribute("authentication", "ROLE_SESSIONUSER");
            return "cart";
        }

        //TODO CHECK
        if (isUserAuthenticated()) {
            if (user.getCart() == null) {
                Users checkUsers = checkUserAndCart(JSESSIONID);
                if (checkUsers.getCart() != null) {
                    model.addAttribute("cart", checkUsers.getCart());
                    double totalPrice = calculateTotalPrice(checkUsers.getCart());
                    model.addAttribute("authentication", user.getRole());
                    model.addAttribute("totalPrice", totalPrice);
                     return "cart";
                }
                //TODO HERE FIX IMPORTANT
            }


            Optional<Cart> cartOptional = cartService.findById(user.getCart().getId());
            // TODO HERE FIX IMPORTANT
           // if ("ROLE_ADMIN".equals(user.getRole()) || ("ROLE_USER".equals(user.getRole()) && user.getCart().getProducts().isEmpty())) {

            //}

            Cart cart = cartOptional.orElse(null);

            //TODO HERE CHECK
//            if (cart != null) {
//                model.addAttribute("cart", cart);
//                double totalPrice = calculateTotalPrice(cart);
//                model.addAttribute("authentication", user.getRole());
//                model.addAttribute("totalPrice", totalPrice);
//            }
        if (user.getCart() != null && cartService.findByName(JSESSIONID) != null) {
             cart = user.getCart();
            Cart cartFromSession = cartService.findByName(JSESSIONID);

            List<Products> products = cartFromSession.getProducts();
            List<Products> productsFromDB = cart.getProducts();
            productsFromDB.addAll(products);
            cart.setProducts(productsFromDB);
            cartService.save(cart);
            session.removeAttribute("cart");
            model.addAttribute("cart", cart);
            double totalPrice = calculateTotalPrice(cart);
            model.addAttribute("authentication", user.getRole());
            model.addAttribute("totalPrice", totalPrice);
        } else if (user.getCart() != null && cartService.findByName(JSESSIONID) == null) {
            cart = user.getCart();
            model.addAttribute("cart", cart);
            double totalPrice = calculateTotalPrice(cart);
            model.addAttribute("authentication", user.getRole());
            model.addAttribute("totalPrice", totalPrice);
        } else {
            model.addAttribute("emptyCart", true);
        }
            JSESSIONID = null;

        System.out.println(session.getId());
        return "cart";
    }
        return "cart";
}

// В вашем контроллере
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        Optional<Products> productOptional = productsRepository.findById(id);

        if (!productOptional.isPresent()) {
            return "redirect:/";
        }
        Products product = productOptional.get();

        Users user = (Users) getCurrentUser();
        Cart cart = null;

        // Если пользователь не авторизован, то создаем корзину на основе сессии.
        //СОВСЕМ 0
        if (user == null && cartService.findByName(session.getId()) == null) {
            String sessionId = session.getId();
            cart = new Cart();
            cart.setName(sessionId);
            Users usersSession = new Users();
            usersSession.setEmail(sessionId);
            cart.setUsers(usersSession);
            List<Products> products = new ArrayList<>();
            products.add(product);
            cart.setProducts(products);
            usersRepository.save(usersSession);
            cartService.save(cart);
            usersSession.setCart(cart);

            return "redirect:" + referer;
        }
        // Если у пользователя уже есть корзина, то используем ее
        // Иначе создаем новую корзину
        //TODO HERE
        if (user == null && cartService.findByName(session.getId()) != null) {
            Cart cartSessionWithProducts = cartService.findByName(session.getId());
            List<Products> products = cartSessionWithProducts.getProducts();

            if (products.contains(product)) {
                for (Products p : products) {
                    if (p.getId().equals(product.getId())) {
                        p.setQuantity(p.getQuantity() + quantity);
                    }
                }
            } else {
                product.setQuantity(quantity);
                products.add(product);
            }
            cartSessionWithProducts.setProducts(products);

            // Сохраняем корзину
            cartService.save(cartSessionWithProducts);

            return "redirect:" + referer;
        }
        if (isUserAuthenticated()) {
            if (user.getCart() != null) {
                cart = user.getCart();
            } else {
                cart = new Cart();
                cart.setName(user.getEmail());
                cart.setPhone(user.getPhone());
                cart.setUsers(user);
//            cart.setProducts(user.getCart().getProducts());
                // Save the user before saving the cart
                usersRepository.save(user);
                cartService.save(cart);
                user.setCart(cart);
                usersRepository.save(user);
            }
        }
        // Добавляем продукт в список продуктов корзины
        List<Products> productList = cart.getProducts();
        if (productList == null) {
            productList = new ArrayList<>();
        }
//        for (int i = 0; i < quantity; i++) {
//            productList.add(product);
//        }
        if (productList.contains(product)) {
            for (Products p : productList) {
                if (p.getId().equals(product.getId())) {
                    p.setQuantity(p.getQuantity() + quantity);
                }
            }
        } else {
            product.setQuantity(quantity);
            productList.add(product);
        }
        //product.setQuantity(quantity);
        //  productList.add(product);

        cart.setProducts(productList);

        // Сохраняем корзину
        cartService.save(cart);


        return "redirect:" + referer;
    }

    @PostMapping("/change/{id}")
    public String changeQuantity(@PathVariable Long id, @RequestParam("quantity") int quantity) {
        Optional<Products> productOptional = productsRepository.findById(id);
        if (!productOptional.isPresent()) {
            return "redirect:/";
        }
        Products product = productOptional.get();

        Users user = (Users) getCurrentUser();
        Cart cart;
        if (user.getCart() != null) {
            cart = user.getCart();
        } else {
            cart = new Cart();
            cartService.save(cart);
            user.setCart(cart);
            usersRepository.save(user);
        }

        // Добавляем продукт в список продуктов корзины
        List<Products> productList = cart.getProducts();
        if (productList == null) {
            productList = new ArrayList<>();
        }
//        for (int i = 0; i < quantity; i++) {
//            productList.add(product);
//        }
        if (productList.contains(product)) {
            for (Products p : productList) {
                if (p.getId().equals(product.getId())) {
                    p.setQuantity(quantity);
                }
            }
        } else {
            product.setQuantity(quantity);
        }
        //product.setQuantity(quantity);
        //  productList.add(product);

        cart.setProducts(productList);

        // Сохраняем корзину
        cartService.save(cart);

        return "redirect:/cart";
    }

    @GetMapping("/add")
    public String getProductToCart() {
        return "redirect:/";
    }

    @PostMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable Long id) {
        Users user = (Users) getCurrentUser();
        Long cartId = user.getCart().getId();
        Cart cart = cartService.findById(cartId).get();
        List<Products> products = cart.getProducts();
        products.removeIf(product -> product.getId().equals(id));
        cart.setProducts(products);
        cartService.save(cart);
        return "redirect:/cart";
    }

    public boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
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

    public double calculateTotalPrice(Cart cart) {
        double totalPrice = 0.0;

        for (Products product : cart.getProducts()) {
            totalPrice += product.getPrice() * product.getQuantity();
        }

        return totalPrice;
    }

    public Users checkUserAndCart(String JSESSIONID) {
        Users userCurrent = (Users) getCurrentUser();
//        if (userCurrent == null) {
//            return null;
//        }
        Users userFromDB = usersRepository.findUsersByEmail(userCurrent.getEmail());
        if (userFromDB.getCart() == null) {
//            Cart cart = new Cart();
//            cart.setUsers(userFromDB);
//            cartService.save(cart);
            Cart cartFromSession = cartService.findByName(JSESSIONID);
            userFromDB.setCart(cartFromSession);
            usersRepository.save(userFromDB);
            return userFromDB;
        } else if (userFromDB.getCart() != null && cartService.findByName(JSESSIONID) != null) {
            Cart cartFromSession = cartService.findByName(JSESSIONID);
            List<Products> products = cartFromSession.getProducts();
            List<Products> productsFromDB = userFromDB.getCart().getProducts();
            productsFromDB.addAll(products);
            userFromDB.getCart().setProducts(productsFromDB);
            usersRepository.save(userFromDB);
            session.removeAttribute("cart");
            return userFromDB;

        }

        return userFromDB;
    }


}