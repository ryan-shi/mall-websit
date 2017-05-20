package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
