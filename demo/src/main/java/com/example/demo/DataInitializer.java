package com.example.demo;

import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(MemberRepository memberRepository, ProductRepository productRepository, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        return args -> {
            try {
                Member member1 = new Member("John Cena", "cena@demo.com", "123456");
                Member member2 = new Member("aaaaa", "aaaaa@demo.com", "654321");
                memberRepository.saveAll(List.of(member1, member2));

                Product product1 = new Product("Product 1", 10.0, 100);
                Product product2 = new Product("Product 2", 20.0, 50);
                productRepository.saveAll(List.of(product1, product2));

                Order order1 = new Order(member1, null, "2023-01-01");
                Order order2 = new Order(member2, null, "2023-01-02");
                orderRepository.saveAll(List.of(order1, order2));

                OrderDetail orderDetail1 = new OrderDetail(order1, product1, 2);
                OrderDetail orderDetail2 = new OrderDetail(order1, product2, 1);
                OrderDetail orderDetail3 = new OrderDetail(order2, product2, 3);
                orderDetailRepository.saveAll(List.of(orderDetail1, orderDetail2, orderDetail3));

                order1.setOrderDetails(List.of(orderDetail1, orderDetail2));
                order2.setOrderDetails(List.of(orderDetail3));
                orderRepository.saveAll(List.of(order1, order2));

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}

