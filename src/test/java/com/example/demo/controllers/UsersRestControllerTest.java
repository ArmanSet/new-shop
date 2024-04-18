//package com.example.demo.controllers;
//
//
//import com.example.demo.entity.Factory;
//import com.example.demo.entity.Users;
//import com.example.demo.repository.UsersRepository;
//import com.example.demo.service.UsersService;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//
//import java.util.List;
//
//import static org.springframework.test.web.client.ExpectedCount.times;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//
////@Testcontainers
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = {
//        "spring.liquibase.change-log=classpath:/db/db.changelog-it.xml"
//})
////@SpringBootTest
//@Testcontainers
//public class UsersRestControllerTest {
//
//    @Container
//    public static PostgreSQLContainer sqlContainer = new PostgreSQLContainer("postgres:13.3").withDatabaseName("cc").withUsername("sa").withPassword("sa");
//
//    @LocalServerPort
//    Integer port;
//
//    @Autowired
//    UsersService usersService;
//
//    @Autowired
//    UsersRepository usersRepository;
//
//    @Autowired
//    UsersRestController usersRestController;
//
//
//    @DynamicPropertySource
//    static void postgresqlProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
//        registry.add("spring.datasource.password", sqlContainer::getPassword);
//        registry.add("spring.datasource.username", sqlContainer::getEmail);
//    }
//
//    @BeforeAll
//    static void startAll() {
//        sqlContainer.start();
//    }
//
//    @AfterAll
//    static void stopAll() {
//        sqlContainer.stop();
//    }
//
//    @Test
//    public void testGetUsers() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> http = new HttpEntity<>(headers);
//        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
//        mockServer.expect(times(1), requestTo("http://localhost:"+port+"/users")).andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:"+port+"/users", HttpMethod.GET, http, String.class);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        mockServer.verify();
//
//    }
//    @Test
//    public void testCreateUser() {
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
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> http = new HttpEntity<>(" {\n" +
//                "        \"username\": \"Armen\",\n" +
//                "        \"password\": \"AZERTY\",\n" +
//                "        \"role\": \"USER\",\n" +
//                "        \"factory\": {\n" +
//                "                    \"name\" : \"Google\",\n" +
//                "                    \"address\" : \"Ninove\"\n" +
//                "\n" +
//                "        }\n" +
//                "    }",headers);
//        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
//
//        mockServer.expect(times(1), requestTo("http://localhost:"+port+"/users")).andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:"+port+"/users", HttpMethod.POST, http, String.class);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertEquals("{}", response.getBody());
//        mockServer.verify();
//
//    }
//
//}