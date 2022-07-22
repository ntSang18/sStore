package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
