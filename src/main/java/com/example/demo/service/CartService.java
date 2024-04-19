package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public double calculateTotalPrice(Cart cart) {
        return cart.getProducts().stream()
                .mapToDouble(product -> (product.getPrice() * product.getQuantity()))
                .sum();
    }

    public void clearCart(Cart cart) {
        cart.getProducts().clear();
        cartRepository.delete(cart);
    }


}