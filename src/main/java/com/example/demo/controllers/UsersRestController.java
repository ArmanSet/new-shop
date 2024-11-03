package com.example.demo.controllers;

import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersRestController {
    private final  UsersService usersService;



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

}
