package com.example.demo.repository;

import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByIdAndMember(Long orderId, Member member, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE od.product.name = :productName AND o.member = :member")
    Page<Order> findByProductNameAndMember(String productName, Member member, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.purchaseDate = :purchaseDate AND o.member = :member")
    Page<Order> findByPurchaseDateAndMember(String purchaseDate, Member member, Pageable pageable);

    Page<Order> findByMember(Member member, Pageable pageable);
}
