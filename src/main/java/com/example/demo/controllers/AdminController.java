package com.example.demo.controllers;

//import com.example.demo.entity.Factory;
import com.example.demo.entity.Users;
//import com.example.demo.service.FactoryService;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/create")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UsersService usersService;
//    private final FactoryService factoryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    static String factoryName;


    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("user", new Users());
//        model.addAttribute("factory", new Factory());
        return "signup";
    }

    @PostMapping("/admin")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postAdmin(@ModelAttribute Users users) {
        users.setRole("ROLE_ADMIN");
        usersService.save(users);

        return "redirect:/create/list"; // Redirect to the list of admins after creating a new one
    }

//    @PostMapping("/factory")
//    public String postFactory(@ModelAttribute Factory factory) {
//        factoryName = factory.getName();
//        factoryService.save(factory);
//        return "redirect:/create/list"; // Redirect to the list of admins after creating a new one
//    }

    @GetMapping("/list")
    public String getAdmins(Model model) {

        List<Users> admins = usersService.findUserByRole("ROLE_ADMIN");
        model.addAttribute("admin", admins);
        return "admin";
    }

}