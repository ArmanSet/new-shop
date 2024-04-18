package com.example.demo.controllers;

import com.example.demo.entity.Factory;
import com.example.demo.entity.Users;
import com.example.demo.service.FactoryService;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/create")
@Controller
public class AdminController {
    UsersService usersService;
    FactoryService factoryService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

   static String factoryName;

    @Autowired
    public AdminController(UsersService usersService, FactoryService factoryService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersService = usersService;
        this.factoryService = factoryService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }


    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("factory", new Factory());
        return "signup";
    }

    @PostMapping("/admin")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postAdmin(@ModelAttribute Users users) {

        usersService.save(users, factoryName);

        return "redirect:/create/list"; // Redirect to the list of admins after creating a new one
    }

    @PostMapping("/factory")
    public String postFactory(@ModelAttribute Factory factory) {
        factoryName = factory.getName();
        factoryService.save(factory);
        return "redirect:/create/list"; // Redirect to the list of admins after creating a new one
    }

//    @GetMapping("/factory")
//    public String getFactory(Model model) {
//        model.addAttribute("factory", new Factory());
//        return "createadmin";
//    }

    @GetMapping("/list")
    public String getAdmins(Model model) {

        List<Users> admins = usersService.findUserByRole("ROLE_ADMIN");
        model.addAttribute("admin", admins);
        return "admin";
    }

}