package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String buyerEmail;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    // Total Amount
    private BigDecimal orderAmount;

    /**
     * default 0: new order.
     */

    @ColumnDefault("0")
    private Integer orderStatus;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;


    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderProducts> orderProducts;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users user;
//
//    @ManyToOne
//    @JoinColumn(name = "cart_id")
//    private Cart cart;

}