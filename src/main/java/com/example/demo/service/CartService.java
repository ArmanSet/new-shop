package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.OrderProducts;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final OrderProductsService orderProductsService;
    private final UsersRepository usersRepository;

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
                .filter(orderProduct -> orderProduct.getProducts() != null)
                .flatMap(orderProduct -> orderProduct.getProducts().stream()
                        .map(product -> product.getPrice() * orderProduct.getQuantity()))
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    //todo START BEST VERSION
//     Перетасовка продуктов из множества OrderProducts в один OrderProducts для отображения
    public Cart convertProductsFromManyOrderProductsToOneForShow(Cart cart) {
        Map<Products, Integer> productQuantityMap = new HashMap<>();
        for (OrderProducts orderProduct : cart.getOrderProducts()) {
            if (orderProduct.getProducts() != null) {
                for (Products product : orderProduct.getProducts()) {
                    if (productQuantityMap.containsKey(product)) {
                        productQuantityMap.put(product, productQuantityMap.get(product) + orderProduct.getQuantity());
                    } else {
                        productQuantityMap.put(product, orderProduct.getQuantity());
                    }
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

    //todo END
//    public void convertProductsFromManyOrderProductsToOneForShow(Cart cart) {
//        Map<Products, Integer> productQuantityMap = new HashMap<>();
//        for (OrderProducts orderProduct : cart.getOrderProducts()) {
//            for (Products product : orderProduct.getProducts()) {
//                if (productQuantityMap.containsKey(product)) {
//                    productQuantityMap.put(product, productQuantityMap.get(product) + orderProduct.getQuantity());
//                } else {
//                    productQuantityMap.put(product, orderProduct.getQuantity());
//                }
//            }
//        }
//
//        List<OrderProducts> newOrderProductsList = new ArrayList<>();
//        for (Map.Entry<Products, Integer> entry : productQuantityMap.entrySet()) {
//            OrderProducts newOrderProduct = new OrderProducts();
//            newOrderProduct.setProducts(Collections.singletonList(entry.getKey()));
//            newOrderProduct.setQuantity(entry.getValue());
//            newOrderProductsList.add(newOrderProduct);
//        }
//
//        cart.setOrderProducts(newOrderProductsList);
//    }

    public Cart findCartByName(String uuid) {
        return cartRepository.findCartByName(uuid);
    }
    //TODO Переделать метод CRITICAL
//    @Transactional
//    public Cart mergeCarts(Users user, HttpServletRequest request) {
//        Cart cartFromSession = checkInCockieIfCartExist(request);
//        Cart cartFromDb = user.getCart();
//        cartFromDb.setId(user.getCart().getId());
//        cartFromDb.setUsers(user);
//        cartFromDb.setName(user.getName());
//        cartFromDb.setAddress(user.getAddress());
//        cartFromDb.setPhone(user.getPhone());
//        cartFromDb.setEmail(user.getEmail());
//        if (cartFromDb.getOrderProducts() == null) {
//            cartFromDb.setOrderProducts(new ArrayList<>());
//        }
//        cartFromDb.getOrderProducts().addAll(cartFromSession.getOrderProducts());
//        convertProductsFromManyOrderProductsToOneForShow(cartFromDb);
//        cartRepository.save(cartFromDb);
//        return cartFromDb;
//    }
//    @Transactional
//    public Cart mergeCarts(Users user, HttpServletRequest request) {
//        Cart cartFromSession = checkInCockieIfCartExist(request);
//
//        // Check if user has an existing cart in the database
//        Cart cartFromDb = user.getCart();
//
//        // If user doesn't have a cart, create a new one from session cart
//        if (cartFromDb == null) {
//            cartFromDb = new Cart();
//            cartFromDb.setName(user.getName());
//            cartFromDb.setAddress(user.getAddress());
//            cartFromDb.setPhone(user.getPhone());
//            cartFromDb.setEmail(user.getEmail());
//            cartFromDb.setUsers(user);
//            cartFromDb.setOrderProducts(cartFromSession.getOrderProducts());
//        } else {
//            // User has a cart, merge session cart products into it
//            cartFromDb.getOrderProducts().addAll(cartFromSession.getOrderProducts());
//        }
//
//        // After merging, convert products for display (optional)
//        convertProductsFromManyOrderProductsToOneForShow(cartFromDb);
//
//        // Save the merged cart to the database
//        cartRepository.save(cartFromDb);
//
//        return cartFromDb;
//    }
    @Transactional
    public Cart mergeCarts(Users user, HttpServletRequest request) {
        Cart cartFromSession = checkInCockieIfCartExist(request);

        // Check if user has an existing cart in the database
        Cart cartFromDb = user.getCart();

        // If user doesn't have a cart, create a new one from session cart
        if (cartFromDb == null) {
            cartFromDb = new Cart();
            cartFromDb.setName(user.getName());
            cartFromDb.setAddress(user.getAddress());
            cartFromDb.setPhone(user.getPhone());
            cartFromDb.setEmail(user.getEmail());
            cartFromDb.setUsers(user);
            cartFromDb.setOrderProducts(cartFromSession.getOrderProducts());
            cartRepository.save(cartFromDb); // <-- Save the newly created cart
        } else {
            // User has a cart, merge session cart products into it
//            cartFromDb.getOrderProducts().addAll(cartFromSession.getOrderProducts());
            List<OrderProducts> orderProducts = cartFromSession.getOrderProducts();
            for (OrderProducts orderProduct : orderProducts) {
                orderProduct.setCart(cartFromDb);
                cartFromDb.setOrderProducts(orderProducts);
                orderProductsService.save(orderProduct);
            }
            cartFromSession.setOrderProducts(new ArrayList<>());
            cartRepository.save(cartFromDb); // <-- Save the merged cart
        }

        // After merging, convert products for display (optional)
        convertProductsFromManyOrderProductsToOneForShow(cartFromDb);

        // No need to save the cart here, it was already saved earlier in this method
        // cartRepository.save(cartFromDb); <-- Not needed

        return cartFromDb;
    }



    public Cart checkInCockieIfCartExist(HttpServletRequest request) {
        String uuid = getCookieValue(request, "uuid");
        if (uuid == null) {
            return null;
        }
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

    public void createCartForUser(Users user) {
        if (user.getCart() == null) {
            Cart cart = new Cart();
            cart.setEmail(user.getEmail());
            cart.setAddress(user.getAddress());
            cart.setName(user.getName());
            cart.setPhone(user.getPhone());
            cart.setUsers(user);
            user.setCart(cart);
            cartRepository.save(cart);
            usersRepository.save(user);
            //return "redirect:" + referer;
        }
    }
    public void clearCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    public void createCartForGuest(String uuid,List<OrderProducts> list) {
        Cart cart = new Cart();
        cart.setName(uuid);
        cart.setOrderProducts(list);
        cartRepository.save(cart);

    }



    public void deleteById(Long id) {
        cartRepository.deleteById(id);
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