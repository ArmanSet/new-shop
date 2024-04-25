package com.example.demo.controllers;


import com.example.demo.entity.Category;
import com.example.demo.entity.Products;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Controller()
@RequiredArgsConstructor
public class MainController {
    private final ProductsService productsService;
    private final CategoryService categoryService;
    private UUID uuid;

    @GetMapping("/")
    public String home(Model model, HttpServletResponse response) {
        if (uuid != null) {
            System.out.println("UUID is not null"+uuid.toString());
        } else {
            uuid = UUID.randomUUID();
            Cookie cookie = new Cookie("uuid", uuid.toString());

// Set cookie attributes (optional)
            cookie.setMaxAge(60 * 60 * 24); // Set cookie to expire in 1 day// Get the response object
            response.addCookie(cookie);

        }


        List<Products> products = productsService.findAll();
//        model.addAttribute("title", "Home");
        model.addAttribute("products", products);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = false;
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_USER")) {
                    isAuthenticated = true;
                    break;
                } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    isAuthenticated = true;
                    break;
                } else {
                    isAuthenticated = false;
                }
            }
        }

        Collection<? extends GrantedAuthority> authoritiesForProductPageDeleteItems = authentication.getAuthorities();
        for (GrantedAuthority authority : authoritiesForProductPageDeleteItems) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("isAdmin", true);
                break;
            } else {
                model.addAttribute("isAdmin", false);
            }
        }

        System.out.println(isAuthenticated);
        model.addAttribute("isAuthenticated", isAuthenticated);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "index";
    }


}