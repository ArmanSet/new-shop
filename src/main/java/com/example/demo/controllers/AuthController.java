package com.example.demo.controllers;


import com.example.demo.entity.Users;
//import com.example.demo.service.AuthService;
import com.example.demo.service.AuthService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    UsersService usersService;
    AuthService authService;
    private AuthenticationManager authenticationManager;


    PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager,UsersService usersService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login(Model model) {
        // Создание нового объекта пользователя
        Users users = new Users();


        // Добавление объекта пользователя в модель
        model.addAttribute("user", users);

        // Возврат имени представления "login.html"
        return "login";
    }

    @PostMapping("/login")
    public String submitLogin(@ModelAttribute Users user) {
        // Проверка наличия пользователя в базе данных
        Users userFromDB = usersService.findUsersByEmail(user.getEmail());
        return "redirect:/users/show";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}