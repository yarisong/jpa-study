package jpabook.jpashop.repository;

import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;

import jpabook.jpashop.domain.Order;

public interface OrderRepositorySpring extends JpaRepository<Order, Long> {
	
	@Override	
    List<Order> findAll();
}


//
//@EntityGraph(attributePaths = {"orderItems", "member"}, type = EntityGraph.EntityGraphType.FETCH)