package com.example.demo.controllers;

import com.example.demo.entity.Category;
import com.example.demo.entity.Products;
import com.example.demo.entity.Subcategory;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductsService;
import com.example.demo.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "categories";
    }
    @GetMapping("/subcategory")
    public String getSubcategories(Model model) {
        List<Subcategory> subcategories = subcategoryService.findAll();
        model.addAttribute("subcategories", subcategories);
        return "subcategories";
    }
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