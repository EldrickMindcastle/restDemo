package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    private String purchaseDate;

    // Constructors, getters, and setters
    public Order() {}

    public Order(Member member, List<OrderDetail> orderDetails, String purchaseDate) {
        this.member = member;
        this.orderDetails = orderDetails;
        this.purchaseDate = purchaseDate;
    }

}
