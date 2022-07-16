package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
