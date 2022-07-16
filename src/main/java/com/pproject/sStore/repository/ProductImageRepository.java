package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
