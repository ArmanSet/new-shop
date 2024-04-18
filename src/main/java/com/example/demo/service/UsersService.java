package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.FactoryRepository;
import com.example.demo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    UsersRepository usersRepository;
    FactoryRepository factoryRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, FactoryRepository factoryRepository) {
        this.usersRepository = usersRepository;
        this.factoryRepository = factoryRepository;
    }

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
        newUsers.setFactory(factoryRepository.findByName(factoryName));

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
}

