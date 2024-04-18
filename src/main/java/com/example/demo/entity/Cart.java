package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;



   @OneToOne(mappedBy = "cart")
    private Users users;

   @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
              name = "cart_products",
              joinColumns = @JoinColumn(name = "cart_id"),
              inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    private List<Products> products;
//
//   @OneToMany(mappedBy = "cart")
//    private List<Order> orders;

}