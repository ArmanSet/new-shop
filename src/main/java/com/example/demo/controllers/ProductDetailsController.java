package com.example.demo.controllers;

import com.example.demo.entity.Category;
import com.example.demo.entity.Products;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/product/details")
@RequiredArgsConstructor
public class ProductDetailsController {
    private final ProductsService productsService;
    private final CategoryService categoryService;

    @RequestMapping("/{id}")
    public String showProductsDetails(@PathVariable("id") Long id, Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", productsService.findProductById(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = !(authentication instanceof AnonymousAuthenticationToken);
//        boolean authenticated = authentication.isAuthenticated();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = false;
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAuthenticated",isAuthenticated);
        return "product-details";
    }
}