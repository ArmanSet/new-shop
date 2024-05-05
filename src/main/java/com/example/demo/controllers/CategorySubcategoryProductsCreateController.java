package com.example.demo.controllers;

import com.example.demo.entity.Category;
import com.example.demo.entity.Products;
import com.example.demo.entity.Subcategory;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductsService;
import com.example.demo.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RequestMapping("/category")
@Controller
@RequiredArgsConstructor
public class CategorySubcategoryProductsCreateController {
    private final CategoryService categoryService;
    private final ProductsService productsService;
    private final SubcategoryService subcategoryService;

    @GetMapping("/create")
    public String getCategory(Model model) {
        List<Category> categoryServiceAll = categoryService.findAll();
        List<Subcategory> subcategoryServiceAll = subcategoryService.findAll();
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryServiceAll);
        model.addAttribute("subcategories", subcategoryServiceAll);
        model.addAttribute("subcategory", new Subcategory());
        model.addAttribute("products", new Products());
        return "category-subcategory-products-create";
    }


    @PostMapping("/create-products")
    public String postProducts(@ModelAttribute Products products) {
        Long categoryID = products.getSubcategory().getId();
//        Category category = categoryService.getCategoryById(categoryID);
        Subcategory subCategory = subcategoryService.getSubcategoryById(categoryID);
        products.setSubcategory(subCategory);
        productsService.save(products);
        return "redirect:/products/list";
    }

    @PostMapping("/create-subcategory")
    public String postSubcategory(@ModelAttribute Subcategory subcategory) {
        Long categoryID = subcategory.getCategory().getId();
        Category category = categoryService.getCategoryById(categoryID);
        subcategory.setCategory(category);
        subcategoryService.save(subcategory);
        return "redirect:/products/list";
    }

    @PostMapping("/create-category")
    public String postCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/products/list";
    }


//    @PostMapping("/create-products")
//    public String postProducts(@ModelAttribute Products products) {
//        Long categoryID = products.getCategory().getId();
//        Category category = categoryService.getCategoryById(categoryID);
//        products.setCategory(category);
//        productsService.save(products);
//        return "redirect:/products/list";
//    }

    @GetMapping("/")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
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
        return "categories";
    }

    @GetMapping("/subcategory")
    public String getSubcategories(Model model) {
        List<Subcategory> subcategories = subcategoryService.findAll();
        model.addAttribute("subcategories", subcategories);
        return "subcategories";
    }

//    @GetMapping("/subcategory/{id}")
//    public String getSubcategory(Model model, @PathVariable Long id) {
//        List<Subcategory> subcategories = categoryService.getSubcategoryByCategoryId(id);
//        model.addAttribute("subcategories", subcategories);
//        return "/subcategories";
//    }
//
//    @GetMapping("subcategory/products/{id}")
//    public String getProducts(Model model, @PathVariable Long id) {
//        List<Products> products = subcategoryService.getProductsBySubcategoryId(id);
//        model.addAttribute("products", products);
//        return "/products";
//    }


    @GetMapping("/catalog")
    public String getCatalog(Model model) {
        List<Products> products = productsService.findAll();
        List<Subcategory> subcategories = subcategoryService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("categories", categories);
        return "/sections/catalog";
    }


}