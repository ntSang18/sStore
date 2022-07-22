package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
