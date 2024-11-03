//package com.example.demo.controllers;
//
//import com.example.demo.entity.Factory;
//import com.example.demo.entity.Users;
//import com.example.demo.repository.UsersRepository;
//import com.example.demo.service.UsersService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@AutoConfigureMockMvc
//@SpringBootTest
//public class UserControllerMockTest {
//    @Autowired
//    UsersRepository usersRepository;
//    @Autowired
//    UsersService usersService;
//
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Test
//    void testGetUsers() throws Exception {
//        when(usersRepository.findAll()).thenReturn(List.of(new Users()));
////        when(usersController.getUsers()).thenReturn(List.of(new Users()));
//        mockMvc.perform(get("/users")).andExpect(status().isOk());
//        Assertions.assertEquals(1, usersRepository.findAll().size());
//        verify(usersRepository).findAll();
////        verify(usersController).getUsers();
//
//    }
//
//    @Test
//    void testCreateUser() throws Exception {
//        Factory factory = new Factory();
//        factory.setName("test");
//        factory.setAddress("test");
//
//        Users users = new Users();
//        users.setEmail("test");
//        users.setPassword("test");
//        users.setRole("test");
//        users.setFactory(factory);
//        factory.setUsers(List.of(users));
////        usersService.save(users);
//
//        MvcResult result = mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"test\",\"password\":\"test\",\"role\":\"test\"}"))
//                .andExpect(status().isOk())
//                .andReturn();
//
////        Users us = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Users.class);
////
////        when(usersService.save(users)).thenReturn(users);
////      Users us = (Users) mockMvc.perform(post("/users")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content("{\"username\":\"test\",\"password\":\"test\",\"role\":\"test\"}"))
////                .andExpect(status().isOk());
////        System.out.println(us.getEmail());
//        Assertions.assertEquals(6, usersRepository.findAll().size());
////        verify(usersRepository, times(1)).save(users);
//
//    }
//
//}