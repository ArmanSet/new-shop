package com.example.demo.controllers;


import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;

@Controller
public class UsersShowController {
    @Autowired
    UsersService usersService;

    //    @PostMapping("/users/show")
//    public String showUsers() {
//        return "users";
//    }
    @GetMapping("/users/show")
    public String showUsersGet(Model model) {
        List<Users> users = usersService.findAll();
        model.addAttribute("user", users);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        String role = null;
        for (GrantedAuthority grantedAuthority : roles) {
             role = grantedAuthority.getAuthority();
         //   System.out.println(grantedAuthority.getAuthority());
        }
       // System.out.println(role);
        model.addAttribute("role",role);

        return "users";
    }

    @PostMapping("/users/create")
    public String createUsersPost(@ModelAttribute Users users) {
        usersService.userSave(users);
        return "redirect:/users/show";
    }
    @GetMapping("/users/create")
    public String createUsersGet(Model model) {
        model.addAttribute("user", new Users());
        return "user-signup";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUsersPost(@PathVariable Long id) {
        usersService.delete(id);
        return "redirect:/users/show";
    }
}