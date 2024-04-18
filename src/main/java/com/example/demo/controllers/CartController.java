package com.example.demo.controllers;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;

import com.example.demo.repository.ProductsRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.CartService;
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


    @Autowired
    public CartController(CartService cartService, ProductsRepository productsRepository, UsersRepository usersRepository) {
        this.productsRepository = productsRepository;
        this.cartService = cartService;
        this.usersRepository = usersRepository;
    }

//    @GetMapping
//    public String showCart(Model model) {
//        Users user = (Users) getCurrentUser();
//        if (user==null) {
//            model.addAttribute("authentication", "ROLE_ANONYMOUS");
//            return "cart";
//        }
//        Optional<Cart> cartOptional = cartService.findById(user.getCart().getId());
////        if (!cartOptional.isPresent() || user.getId() == null) {
////            model.addAttribute("empty", true);
////        }
//
//        if (user.getRole()=="ROLE_ADMIN" || user.getRole()=="ROLE_USER" && user.getCart().getProducts().isEmpty()) {
//            model.addAttribute("emptyCart", true);
//        }
//
//        Long id = user.getCart().getId();
//
//        Cart cart = cartService.findById(id).get();
//        model.addAttribute("cart", cart);
//        double totalPrice = calculateTotalPrice(cart);
//        model.addAttribute("authentication", user.getRole());
//        model.addAttribute("totalPrice", totalPrice);
//
//        return "cart";
//    }
@GetMapping
public String showCart(Model model) {
    Users user = (Users) getCurrentUser();
    if (user==null) {
        model.addAttribute("authentication", "ROLE_ANONYMOUS");
        return "cart";
    }

    if (user.getCart() != null) {
        Optional<Cart> cartOptional = cartService.findById(user.getCart().getId());

        if ("ROLE_ADMIN".equals(user.getRole()) || ("ROLE_USER".equals(user.getRole()) && user.getCart().getProducts().isEmpty())) {
            model.addAttribute("emptyCart", true);
        }

        Cart cart = cartOptional.orElse(null);

        if (cart != null) {
            model.addAttribute("cart", cart);
            double totalPrice = calculateTotalPrice(cart);
            model.addAttribute("authentication", user.getRole());
            model.addAttribute("totalPrice", totalPrice);
        }
    } else {
        model.addAttribute("emptyCart", true);
    }

    return "cart";
}

//    @GetMapping("/add/{id}")
//    public String addToCart(@PathVariable Long id) {
//        // Here, you can use the product ID to find the product and add it to the cart.
//        // After that, you can redirect the user to the cart page or wherever you want.
//        List<Products> productList = new ArrayList<>();
//        Optional<Products> productOptional = productsRepository.findById(id);
//        if (!productOptional.isPresent()) {
//            return "redirect:/";
//        }
//        Products products = productOptional.get();
//
//        Cart cart;
//        Users user = (Users) getCurrentUser();
//
//        if (user.getCart() != null) {
//            cart = user.getCart();
//        } else {
//            cart = new Cart();
//
//
//        }
//        productList.add(products);
//
//        cart.setProducts(productList);
//        cart.setName(user.getEmail());
//        cart.setPhone(user.getPhone());
//        cart.setEmail(user.getEmail());
//
//        cart.setUsers(user);
//        cartService.save(cart);
//        user.setCart(cart);
//        usersRepository.save(user);
//        return "redirect:/";
//    }

    // В вашем контроллере
    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam("quantity") int quantity) {
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
            cart.setName(user.getEmail());
            cart.setPhone(user.getPhone());
            cart.setUsers(user);
//            cart.setProducts(user.getCart().getProducts());
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

        return "redirect:/";
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

//        @GetMapping("/delete/{id}")
//        public String deleteProductFromCart(@PathVariable Long id) {
//            Users user = (Users) getCurrentUser();
//            Long cartId = user.getCart().getId();
//            Cart cart = cartService.findById(cartId).get();
//            List<Products> products = cart.getProducts();
//            products.removeIf(product -> product.getId().equals(id));
//            cart.setProducts(products);
//            cartService.save(cart);
//            return "redirect:/cart";
//        }


}