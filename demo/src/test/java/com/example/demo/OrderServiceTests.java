package com.example.demo;

import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(OrderService.class)
public class OrderServiceTests {

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

    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setup() {

        Member member = new Member("John Doe", "john@example.com", "password");
        memberRepository.save(member);

        Product product1 = new Product("Product 1", 10.0, 100);
        Product product2 = new Product("Product 2", 20.0, 50);
        productRepository.saveAll(List.of(product1, product2));
    }

    @Test
    public void testCreateOrder() {

        Order createdOrder = orderService.createOrder("john@example.com", "password", List.of(1L, 2L), List.of(1, 2));


        assertNotNull(createdOrder);
        assertNotNull(createdOrder.getId());


        Optional<Order> orderFromDb = orderRepository.findById(createdOrder.getId());
        assertTrue(orderFromDb.isPresent());
        assertEquals(createdOrder.getId(), orderFromDb.get().getId());
        assertEquals("john@example.com", orderFromDb.get().getMember().getEmail());
        assertEquals(2, orderFromDb.get().getOrderDetails().size());
    }



    @Test
    public void testGetMembersWithOrderCountGreaterThan() {

        orderService.createOrder("john@example.com", "password", List.of(1L, 2L), List.of(1, 2));
        orderService.createOrder("john@example.com", "password", List.of(1L, 2L), List.of(1, 2));
        orderService.createOrder("john@example.com", "password", List.of(1L, 2L), List.of(1, 2));

        List<Member> members = orderService.getMembersWithOrderCountGreaterThan(2);


        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals("john@example.com", members.get(0).getEmail());
    }
}

