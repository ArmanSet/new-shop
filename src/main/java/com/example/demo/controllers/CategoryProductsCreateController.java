package com.example.demo.controllers;

import com.example.demo.entity.Category;
import com.example.demo.entity.Products;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/category")
@Controller
@RequiredArgsConstructor
public class CategoryProductsCreateController {
    private final  CategoryService categoryService;
    private final  ProductsService productsService;

    @GetMapping("/create")
    public String getCategory(Model model) {
        List<Category> categoryServiceAll = categoryService.findAll();
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryServiceAll);
        model.addAttribute("products", new Products());
        return "category-products-create";
    }
    @PostMapping("/create-category")
    public String postCategory(@ModelAttribute Category category) {
        categoryService.save(category);

        return "redirect:/products/list";
    }
    @PostMapping("/create-products")
    public String postProducts(@ModelAttribute Products products) {
       Long categoryID =  products.getCategory().getId();
        Category category = categoryService.getCategoryById(categoryID);
        products.setCategory(category);
        productsService.save(products);

        return "redirect:/products/list";
    }
   @GetMapping("/")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "categories";
    }

}