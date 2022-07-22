package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.ProductItem;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long>{

}
