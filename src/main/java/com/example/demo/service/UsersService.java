package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderProducts;
import com.example.demo.entity.Users;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final OrderService orderService;
    private final OrderProductsService orderProductsService;
    private final CartRepository cartRepository;


    public List<Users> findAll() {
        List<Users> usersList = usersRepository.findAll();
//        for (Users users : usersList) {
//            System.out.println(users.getFactory().getName());
//            System.out.println(users.getEmail());
//        }
        return usersRepository.findAll();
    }

    public void save(Users users, String factoryName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Users newUsers = new Users();
        newUsers.setEmail(users.getEmail());
        newUsers.setPassword(passwordEncoder.encode(users.getPassword()));
        newUsers.setName(users.getName());
        newUsers.setRole(users.getRole());
        newUsers.setAddress(users.getAddress());
        newUsers.setPhone(users.getPhone());
//        Factory factoryFromDB = factoryService.findFactoryByName(factory.getName());
//        newUsers.setFactory(factoryRepository.findByName(factoryName));

        usersRepository.save(newUsers);

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(users.getPassword());
//
//        Factory factoryModel = new Factory();
//        factoryModel.setName(users.getFactory().getName());
//        factoryModel.setAddress(users.getFactory().getAddress());
//        factoryModel.setUsers(users.getFactory().getUsers());
//        factoryRepository.save(factoryModel);
//
//        Users newUsers = new Users();
//        newUsers.setEmail(users.getEmail());
//        newUsers.setPassword(encodedPassword); // save encoded password
//        newUsers.setRole(users.getRole());
//        newUsers.setFactory(factoryModel);


//        usersRepository.save(users);
    }

    public void userSave(Users users) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRole("ROLE_USER");
        usersRepository.save(users);
    }

    public void update(Long id, Users users) {
        Users users1 = usersRepository.findById(id).get();
        users1.setEmail(users.getEmail());
        users1.setPassword(users.getPassword());
        users1.setRole(users.getRole());
        usersRepository.save(users1);
    }

    public void delete(long id) {
        usersRepository.deleteById(id);
    }

    public Users findById(Long id) {
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isPresent()) {
            return usersOptional.get();
        } else {
            return null;
        }
    }

    public Users findUserByName(String username) {
        return usersRepository.findUserByName(username);
    }

    public List<Users> findUserByRole(String role) {
        return usersRepository.findByRole(role);
//        return usersRepository.findUserByRole(role);
    }

    public Users findUsersByEmail(String email) {
        return usersRepository.findUsersByEmail(email);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Users user = usersRepository.findUserByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(user.getRole()));
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                authorities
//        );
//    }

//    public void deleteUser(Long userId) {
//        Users user = usersRepository.findById(userId).get();
//        if (user != null) {
//            List<Order> orders = orderService.findAllOrdersByUsers(user.getEmail());
//            for (Order order : orders) {
//                List<OrderProducts> orderProducts = order.getOrderProducts();
//                for (OrderProducts orderProduct : orderProducts) {
//                    OrderProducts orderProductToDelete = orderProductsService.findById(orderProduct.getId()).get();
//                    if (orderProductToDelete != null) {
//                        orderProductsService.delete(orderProductToDelete);
//                    }
//                }
//                orderService.delete(order.getId());
//            }
//            usersService.delete(userId);
//        }
//    }
@Transactional
public void deleteUser(Long userId) {
    Users user = usersRepository.findById(userId).orElse(null);
    if (user != null) {
        // Удалить OrderProducts, связанные с Order
        List<Order> orders = orderService.findAllOrdersByUsers(user.getEmail());
        for (Order order : orders) {
            List<OrderProducts> orderProducts = order.getOrderProducts();
            for (OrderProducts orderProduct : orderProducts) {
                orderProductsService.delete(orderProduct);
            }
            orderService.delete(order.getId());
        }

        // Удалить OrderProducts, связанные с Cart
        if (user.getCart() != null) {
            List<OrderProducts> cartOrderProducts = user.getCart().getOrderProducts();
            for (OrderProducts orderProduct : cartOrderProducts) {
                orderProductsService.delete(orderProduct);
            }
        }

        // Удалить Cart
        if (user.getCart() != null){
            cartRepository.delete(user.getCart());
        }


        // Удалить Users
        usersRepository.delete(user);
    }
}
}

