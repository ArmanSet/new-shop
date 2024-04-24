package com.example.demo.controllers;


import com.example.demo.entity.Factory;
import com.example.demo.service.FactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factory")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class FactoryRestController {

    private final  FactoryService factoryService;

    @GetMapping("/")
    public List<Factory> getAllFactories() {
      List<Factory> factoryList = factoryService.findAll();
        return factoryList;
    }

    @PostMapping("/create")
    public String createFactory(@RequestBody Factory factory) {
        factoryService.save(factory);
        return "Factory";
    }
}
