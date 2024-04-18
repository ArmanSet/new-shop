package com.example.demo.controllers;


import com.example.demo.entity.Accountant;
import com.example.demo.service.AccountantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accountant")
public class AccountantRestController {
    private AccountantService accountantService;

    @Autowired
    public AccountantRestController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @GetMapping("/findAll")
    public List<Accountant> call() {
        return   accountantService.findAll();
    }

    @PostMapping("/create")
    public String createAccountant(@RequestBody Accountant accountant) {
        accountantService.save(accountant);
        return "OK";
    }


}
