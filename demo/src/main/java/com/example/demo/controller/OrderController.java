package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestParam String email, @RequestParam String password, @RequestParam List<Long> productIds, @RequestParam List<Integer> quantities) {
        return orderService.createOrder(email, password, productIds, quantities);
    }

    @GetMapping
    public PagedModel<EntityModel<Order>> getOrders(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String purchaseDate,
            @RequestParam int page,
            @RequestParam int size) {

        return orderService.getOrders(email, password, orderId, productName, purchaseDate, PageRequest.of(page, size));
    }

    @GetMapping("/getOrderCountGreaterThan")
    public List<Member> getMembersWithOrderCountGreaterThan(@RequestParam int n) {
        return orderService.getMembersWithOrderCountGreaterThan(n);
    }
}
