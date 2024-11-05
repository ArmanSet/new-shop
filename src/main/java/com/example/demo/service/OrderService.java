package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.OrderProductsRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductsRepository;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderProductsRepository orderProductsRepository;
    private final ProductsRepository productsRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UsersRepository userRepository;


//    public void saveOrder(Cart cart, Users users, double totalPrice) {
//        Order order = new Order();
//        order.setBuyerEmail(users.getEmail());
//        order.setBuyerName(users.getName());
//        order.setBuyerPhone(users.getPhone());
//        order.setBuyerAddress(users.getAddress());
////        order.setOrderAmount(new BigDecimal(cart.getTotalPrice()));
//        order.setOrderStatus(0);
////        order.setProducts(cart.getProducts());
//        List<Products> productsForOrder = new ArrayList<>(cart.getProducts());
//        OrderProducts orderProducts = new OrderProducts();
//        List<OrderProducts> orderProductsList = new ArrayList<>();
//
//        for (Products product : productsForOrder) {
//            orderProducts.setProduct(product);
//            orderProducts.setOrder(order);
//            orderProducts.setQuantity(product.getQuantity());
//        }
//        orderProductsList.add(orderProducts);
//        order.setOrderProducts(orderProductsList);
//        order.setCreateTime(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
//        order.setOrderAmount(new BigDecimal(totalPrice));
//        order.setUsers(users);
//        order.setDescription("Order from " + users.getName() + " at " + order.getCreateTime());
//        orderRepository.save(order);
//        cartService.clearCart(cart);
//
//
//    }

    public List<Order> getOrders(String usersEmail) {
        List<Order> orders = orderRepository.findAllByBuyerEmailContaining(usersEmail);
        for (Order order : orders) {
            System.out.println(order.getBuyerEmail());
            System.out.println(order.getBuyerName());
            System.out.println(order.getBuyerPhone());
            System.out.println(order.getBuyerAddress());
            System.out.println(order.getOrderAmount());
            System.out.println(order.getCreateTime());
            System.out.println(order.getDescription());
            System.out.println(order.getOrderStatus());
            for (OrderProducts orderProducts : order.getOrderProducts()) {
//                System.out.println(orderProducts.getProduct().getName());
                System.out.println(orderProducts.getQuantity());
            }
        }
        return orders;
    }
//
//    public double calculateTotalPrice(Cart cart) {
//        return cart.getProducts().stream()
//                .mapToDouble(product -> (product.getPrice() * product.getQuantity()))
//                .sum();
//    }


    public List<Order> findByUserEmail(String name) {
        return orderRepository.findAllByBuyerEmailContaining(name);
    }

    public BigDecimal calculateTotalPrice(Cart cart) {
        return cart.getOrderProducts().stream()
                .flatMap(orderProduct -> orderProduct.getProducts().stream()
                        .map(product -> product.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void save(Order order) {
        orderRepository.save(order);
    }



    //    public List<Order> findAllOrdersByUsers(String email) {
//        List<Order> orders = orderRepository.findAllByUsersEmail(email);
//        for (Order order : orders){
//            List<OrderProducts> orderProducts = orderProductsRepository.findByOrder(order);
//            List<Products> products = new ArrayList<>();
//            for (OrderProducts orderProduct : orderProducts){
//                Products product = productsRepository.findById(orderProduct.getProducts().stream().);
//                products.add(product);
//            }
//            order.setOrderProducts(orderProducts);
//            order.setProducts(products);
//
//        }
//        return orders;
//    }
    public List<Order> findAllOrdersByUsers(String email) {
        List<Order> orders = orderRepository.findAllByBuyerEmailContaining(email);

        for (Order order : orders) {
            List<OrderProducts> orderProducts = order.getOrderProducts();

            List<Products> products = orderProducts.stream()
                    .flatMap(orderProduct -> orderProduct.getProducts().stream())
                    .collect(Collectors.toList());

            // order.setProducts(products); // если у вас есть такой метод в классе Order
        }
        return orders;
    }

    public List<Order> findAllByUsersEmailDistinct(String email) {
        return orderRepository.findAllByUsersEmailWithProducts(email);
    }


    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByEmailContain(String email,String name) {
     return   orderRepository.findByBuyerEmailOrBuyerName(email,name);
    }

//    public List<Order> findAllByEmailAndName(String email, String name) {
//       return orderRepository.findAllByEmailAndName(email,name);
//    }

    public List<Order> findAllByEmailContaining(String email) {
        return orderRepository.findAllByBuyerEmailContaining(email);
    }

    public List<Order> findAllByNameContaining(String name) {
        return orderRepository.findAllByNameContaining(name);
    }

    public List<Order> findAllByBuyerEmailContaining(String email) {
        return orderRepository.findAllByBuyerEmailContaining(email);
    }
    
}