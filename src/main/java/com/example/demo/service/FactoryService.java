package com.example.demo.service;

import com.example.demo.entity.Factory;
import com.example.demo.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactoryService {
    FactoryRepository factoryRepository;
    @Autowired
    public FactoryService(FactoryRepository factoryRepository) {
        this.factoryRepository = factoryRepository;
    }

    public List<Factory> findAll() {
      return  factoryRepository.findAll();
    }

    public void save(Factory factory) {
        factoryRepository.save(factory);
        System.out.println("Factory is created");
    }
    public Factory findById(Long id) {
        return factoryRepository.findById(id).get();
    }

    public Factory findFactoryByName(String s) {
        return factoryRepository.findByName(s);
    }
}
