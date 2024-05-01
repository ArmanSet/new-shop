package com.example.demo.controllers;


import com.example.demo.entity.Subcategory;
import com.example.demo.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/subcategory")
public class SubcategoryController {
    private final SubcategoryService subcategoryService;

    @GetMapping("/list/{id}")
    public String getSubcategory(@PathVariable Long id,Model model ) {
        List<Subcategory> subcategories = subcategoryService.findByCategoryId(id);
        model.addAttribute("subcategories", subcategories);
        return "/subcategories";
    }
    @PostMapping("/list/{id}")
    public String filterSubcategoryByCategoryPost(@PathVariable Long id, Model model) {
        List<Subcategory> subcategories = subcategoryService.findByCategoryId(id);
        model.addAttribute("subcategories", subcategories);
        return "/subcategories";
    }

    @GetMapping("/list")
    public String listSubcategories(Model model) {
        List<Subcategory> subcategories = subcategoryService.findAll();
        model.addAttribute("subcategories", subcategories);
        return "/subcategories";
    }

}