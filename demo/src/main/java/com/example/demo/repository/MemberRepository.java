package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    @Query("SELECT m FROM Member m WHERE (SELECT COUNT(o) FROM Order o WHERE o.member = m) > :n AND EXISTS (SELECT 1 FROM Order o WHERE o.member = m)")
    List<Member> findMembersWithOrderCountGreaterThan(int n);
}
