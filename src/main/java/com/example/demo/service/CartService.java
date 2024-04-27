package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.OrderProducts;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;
import com.example.demo.repository.CartRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void updateCart(Long id, String name, String address, String phone, String email) {
        Cart cart = cartRepository.findById(id).get();
        cart.setName(name);
        cart.setAddress(address);
        cart.setPhone(phone);
        cart.setEmail(email);
        cartRepository.save(cart);
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public boolean existsById(Long id) {
        return cartRepository.existsById(id);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

//    public double calculateTotalPrice(Cart cart) {
//        return cart.getProducts().stream()
//                .mapToDouble(product -> (product.getPrice() * product.getQuantity()))
//                .sum();
//    }
//
//    public void clearCart(Cart cart) {
//        cart.getProducts().clear();
//        cartRepository.delete(cart);
//    }


    public Cart findByName(String id) {
        return cartRepository.findByName(id);
    }

    public void delete(Cart cartFromSession) {
        cartRepository.delete(cartFromSession);
    }


    public double calculateTotalPrice(Cart cart) {
        return cart.getOrderProducts().stream()
                .flatMap(orderProduct -> orderProduct.getProducts().stream()
                        .map(product -> product.getPrice() * orderProduct.getQuantity()))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    // Перетасовка продуктов из множества OrderProducts в один OrderProducts для отображения
    public Cart convertProductsFromManyOrderProductsToOneForShow(Cart cart) {
        Map<Products, Integer> productQuantityMap = new HashMap<>();
        for (OrderProducts orderProduct : cart.getOrderProducts()) {
            for (Products product : orderProduct.getProducts()) {
                if (productQuantityMap.containsKey(product)) {
                    productQuantityMap.put(product, productQuantityMap.get(product) + orderProduct.getQuantity());
                } else {
                    productQuantityMap.put(product, orderProduct.getQuantity());
                }
            }
        }
        // TODO CAN BE CRITICAL
        List<OrderProducts> newOrderProductsList = new ArrayList<>();
        for (Map.Entry<Products, Integer> entry : productQuantityMap.entrySet()) {
            OrderProducts newOrderProduct = new OrderProducts();
            newOrderProduct.setProducts(Collections.singletonList(entry.getKey()));
            newOrderProduct.setQuantity(entry.getValue());
            newOrderProductsList.add(newOrderProduct);
        }

        Cart newCart = new Cart();
        newCart.setOrderProducts(newOrderProductsList);
        return newCart;
    }

    public Cart findCartByName(String uuid) {
        return cartRepository.findCartByName(uuid);
    }
    //TODO Переделать метод CRITICAL
    public Cart mergeCarts(Users user, HttpServletRequest request) {
        if (checkInCockieIfCartExist(request) != null && user.getCart().getOrderProducts().getFirst() != null) {
            Cart cartFromSession = checkInCockieIfCartExist(request);
            Cart cartFromDb = user.getCart();
            cartFromDb.getOrderProducts().addAll(cartFromSession.getOrderProducts());
            // todo HERE
            convertProductsFromManyOrderProductsToOneForShow(cartFromDb);
//            cartRepository.delete(cartFromSession);
            cartRepository.save(cartFromDb);
            return cartFromDb;

        } else if (checkInCockieIfCartExist(request) != null && user.getCart().getOrderProducts().getFirst()==null) {
            Cart cartFromSession = checkInCockieIfCartExist(request);
            Cart cartFromDb = user.getCart();
            cartFromDb.getOrderProducts().addAll(cartFromSession.getOrderProducts());
            convertProductsFromManyOrderProductsToOneForShow(cartFromDb);
//            cartRepository.delete(cartFromSession);
            cartRepository.save(cartFromDb);
            return cartFromDb;
        } else {
            return user.getCart();
        }
    }

    public Cart checkInCockieIfCartExist(HttpServletRequest request) {
        String uuid = getCookieValue(request, "uuid");
        return cartRepository.findCartByName(uuid);

    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        String cookieValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }
        return cookieValue;
    }

//    public Cart convertProductsFromManyOrderProductsToOneForShow(Cart cart) {
//        if (cart == null || cart.getOrderProducts().isEmpty()) {
//            return new Cart(); // Return an empty cart if no products exist
//        }
//
//        // Create a map to store product-quantity pairs
//        Map<Products, Integer> productQuantityMap = new HashMap<>();
//
//        // Iterate through all OrderProducts in the cart
//        for (OrderProducts orderProduct : cart.getOrderProducts()) {
//            for (Products product : orderProduct.getProducts()) {
//                // Check if the product exists in the map
//                if (productQuantityMap.containsKey(product)) {
//                    // Update the quantity for the existing product
//                    productQuantityMap.put(product, productQuantityMap.get(product) + orderProduct.getQuantity());
//                } else {
//                    // Add the new product to the map with its quantity
//                    productQuantityMap.put(product, orderProduct.getQuantity());
//                }
//            }
//        }
//
//        // Create a new list to store unique OrderProducts
//        List<OrderProducts> newOrderProductsList = new ArrayList<>();
//
//        // Iterate through the product-quantity map entries
//        for (Map.Entry<Products, Integer> entry : productQuantityMap.entrySet()) {
//            // Create a new OrderProducts object
//            OrderProducts newOrderProduct = new OrderProducts();
//
//            // Set the product list with the current product
//            newOrderProduct.setProducts(Collections.singletonList(entry.getKey()));
//
//            // Set the quantity with the aggregated quantity
//            newOrderProduct.setQuantity(entry.getValue());
//
//            // Add the new OrderProducts to the list
//            newOrderProductsList.add(newOrderProduct);
//        }
//
//        // Create a new Cart object (optional, can be modified based on your needs)
//        Cart newCart = new Cart();
//
//        // Set the new OrderProducts list to the new cart (optional)
//        newCart.setOrderProducts(newOrderProductsList);
//
//        // Return the new cart or the modified cart (depending on your preference)
//       //  return newOrderProductsList; // Return the list of unique OrderProducts
//        cart.setOrderProducts(newOrderProductsList);
//        return cart;
//    }
}