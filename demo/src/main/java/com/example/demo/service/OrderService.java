package com.example.demo.service;



import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PagedResourcesAssembler<Order> pagedResourcesAssembler;

    //使用樂觀鎖與原子性保證多執行緒下安全不會超賣
    @Transactional
    public Order createOrder(String email, String password, List<Long> productIds, List<Integer> quantities) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            // Create new member if not exists
            member = new Member("New Member", email, password);
            memberRepository.save(member);
        } else {
            // Validate password
            if (!member.getPassword().equals(password)) {
                throw new IllegalArgumentException("Invalid email or password");
            }
        }

        // Generate current system time as purchaseDate
        String purchaseDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        var orderDetails = productIds.stream()
                .map(productId -> {
                    var product = productRepository.findById(productId).orElseThrow();
                    var quantity = quantities.get(productIds.indexOf(productId));

                    // Check stock
                    if (product.getStockQuantity() < quantity) {
                        throw new IllegalStateException("Not enough stock for product: " + product.getName());
                    }

                    // Decrease stock
                    product.setStockQuantity(product.getStockQuantity() - quantity);
                    productRepository.save(product);

                    return new OrderDetail(null, product, quantity);
                })
                .collect(Collectors.toList());

        var order = new Order(member, orderDetails, purchaseDate);
        orderRepository.save(order);
        orderDetails.forEach(orderDetail -> orderDetail.setOrder(order));
        orderDetailRepository.saveAll(orderDetails);

        return order;
    }

    @Transactional(readOnly = true)
    public PagedModel<EntityModel<Order>> getOrders(String email, String password, Long orderId, String productName, String purchaseDate, Pageable pageable) {
        Member member = memberRepository.findByEmail(email);
        if (member == null || !member.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Page<Order> ordersPage;
        if (orderId != null) {
            ordersPage = orderRepository.findByIdAndMember(orderId, member, pageable);
        } else if (productName != null) {
            ordersPage = orderRepository.findByProductNameAndMember(productName, member, pageable);
        } else if (purchaseDate != null) {
            ordersPage = orderRepository.findByPurchaseDateAndMember(purchaseDate, member, pageable);
        } else {
            ordersPage = orderRepository.findByMember(member, pageable);
        }

        return pagedResourcesAssembler.toModel(ordersPage);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembersWithOrderCountGreaterThan(int n) {
        return memberRepository.findMembersWithOrderCountGreaterThan(n);
    }
}
