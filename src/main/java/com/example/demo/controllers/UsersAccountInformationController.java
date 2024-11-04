package com.example.demo.controllers;

import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

@RequestMapping("/users")
public class UsersAccountInformationController {

    UsersService usersService;
    PasswordEncoder passwordEncoder;


    @Autowired
    public UsersAccountInformationController(UsersService usersService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/info")
    public String showUsers(Model model) {
        Users userInfo = getCurrentUser();
//            Users userInfo = (Users) authentication.getPrincipal();

        String role = userInfo.getRole();
        if (role.equals("ROLE_ADMIN")) {
            model.addAttribute("isAdmin", true);
        }

        model.addAttribute("user", userInfo);
        return "account-info";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        Users currentUser = getCurrentUser();
        if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            // Установка нового пароля с хешированием
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            usersService.update(currentUser.getId(),currentUser);
            return "redirect:/users/info";
        }
        return "redirect:/users/info";
    }
    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Users userInfo = null;
        if (principal instanceof UserDetails) {
            String name = ((UserDetails) principal).getUsername();
            userInfo = usersService.findUsersByEmail(name);
        } else {
            return null;
        }
        return userInfo;
    }
}
