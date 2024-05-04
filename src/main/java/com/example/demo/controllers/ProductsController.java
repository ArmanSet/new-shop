package com.example.demo.controllers;

import com.example.demo.entity.Category;
import com.example.demo.entity.Order;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;
import com.example.demo.repository.ProductsRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductsService;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productService;
    private final UsersService usersService;

    @GetMapping("/list/{id}")
    public String getProduct(@PathVariable Long id, Model model) {

        List<Products> products = productService.findBySubcategoryId(id);
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping("/list/{id}")
    public String filterProductsByCategoryPost(@PathVariable Long id, Model model) {
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
        List<Products> products = productService.findBySubcategoryId(id);
        model.addAttribute("products", products);
        return "products";


    }


    @GetMapping("/list")
    public String listProducts(Model model) {

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

        List<Products> products = productService.findAll();

        model.addAttribute("products", products);

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            // Now you can use the username to load your user entity from the database
            Users users = usersService.findUsersByEmail(username);
            model.addAttribute("users", users);
        } else {
            // The principal is not an instance of UserDetails, handle this error
        }


        return "products";
    }


    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products/list";
    }


}