package com.example.demo.controllers;


import com.example.demo.entity.Category;
import com.example.demo.entity.Subcategory;
import com.example.demo.service.CategoryService;
import com.example.demo.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/subcategory")
public class SubcategoryController {
    private final SubcategoryService subcategoryService;
    private final CategoryService categoryService;

    @GetMapping("/list/{id}")
    public String getSubcategory(@PathVariable Long id,Model model ) {
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
        List<Subcategory> subcategories = subcategoryService.findByCategoryId(id);
        model.addAttribute("subcategories", subcategories);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/subcategories";
    }
    @PostMapping("/list/{id}")
    public String filterSubcategoryByCategoryPost(@PathVariable Long id, Model model) {
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
        List<Subcategory> subcategories = subcategoryService.findByCategoryId(id);
        model.addAttribute("subcategories", subcategories);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/subcategories";
    }

    @GetMapping("/list")
    public String listSubcategories(Model model) {
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

        List<Subcategory> subcategories = subcategoryService.findAll();
        model.addAttribute("subcategories", subcategories);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/subcategories";
    }

}