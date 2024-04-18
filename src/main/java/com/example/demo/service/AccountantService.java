package com.example.demo.service;


import com.example.demo.entity.Accountant;
import com.example.demo.entity.Factory;
import com.example.demo.entity.Users;
import com.example.demo.repository.AccountantRepository;
import com.example.demo.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountantService {

    private AccountantRepository accountantRepository;
    private FactoryRepository factoryRepository;

    @Autowired
    public AccountantService(AccountantRepository accountantRepository, FactoryRepository factoryRepository) {
        this.accountantRepository = accountantRepository;
        this.factoryRepository = factoryRepository;
    }

    public List<Accountant> findAll() {
       return accountantRepository.findAll();
    }

    public void save(Accountant accountant) {
//        Factory factory= new Factory();
//        factory.setName(accountant.getFactories().getName());
//        factory.setAddress(accountant.getFactories().getAddress());
//        factory.setAccountants(accountant.getFactories().getAccountants());
//        factoryRepository.save(factory);
//        Accountant newAccountant = new Accountant();
//        newAccountant.setName(accountant.getName());
//        newAccountant.setAddress(accountant.getAddress());
//        newAccountant.setPhone(accountant.getPhone());
//        newAccountant.setAddress(accountant.getAddress());
//        accountantRepository.save(newAccountant);
        Factory factory = new Factory();
        factory.setName(accountant.getFactories().getName());
        factory.setAddress(accountant.getFactories().getAddress());
        factory.setAccountants(accountant.getFactories().getAccountants());
        factoryRepository.save(factory);
        Accountant newAccountant = new Accountant();
        newAccountant.setName(accountant.getName());
        newAccountant.setAddress(accountant.getAddress());
        newAccountant.setPhone(accountant.getPhone());
        newAccountant.setFactories(factory);
        accountantRepository.save(newAccountant);
        factoryRepository.save(factory);
    }
}
