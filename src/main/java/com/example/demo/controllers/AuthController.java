package com.example.demo.controllers;


import com.example.demo.entity.Users;
//import com.example.demo.service.AuthService;
import com.example.demo.service.AuthService;
import com.example.demo.service.CartService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {
    private final UsersService usersService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login(Model model) {
        Users users = new Users();
        model.addAttribute("user", users);
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
//        authService.clearCookie(request,response);

        return "redirect:/login";
    }
}