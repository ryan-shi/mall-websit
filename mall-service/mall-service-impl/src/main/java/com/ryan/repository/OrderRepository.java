package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ryan.domain.Order;

public interface OrderRepository  extends JpaRepository<Order, Long>,JpaSpecificationExecutor<Order>{

}
