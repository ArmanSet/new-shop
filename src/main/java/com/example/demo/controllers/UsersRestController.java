package com.example.demo.controllers;

import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersRestController {
    UsersService usersService;

    @Autowired
    public UsersRestController(UsersService usersService) {
        this.usersService = usersService;
    }


    @GetMapping("/users")
    public List<Users> getUsers() {
        return usersService.findAll();
    }

    @GetMapping("/users/{id}")
    public Users getUser(@PathVariable("id") int id) {
        return usersService.findById((long) id);
    }

    @PostMapping("/users")
    public void postUser(@RequestBody Users users) {
        usersService.userSave(users);
    }

    @PutMapping("/users/{id}")
    public void putAdmin(@PathVariable("id") int id, @RequestBody Users users) {
        usersService.update((long) id, users);
    }

    @DeleteMapping("/users/{id}")
    public void deleteAdmin(@PathVariable("id") int id) {
        usersService.delete((long) id);
    }

//    @PostMapping("/post/users/{username}/{password}/{role}/")
//    public void postUsers(@PathVariable("username") String username,
//                          @PathVariable("password") String password,
//                          @PathVariable("role") String role) {
//        System.out.println(username);
//        Users users = new Users();
//        users.setEmail(username);
//        users.setPassword(password);
//        users.setRole(role);
//        usersService.save(users);
//    }

}
