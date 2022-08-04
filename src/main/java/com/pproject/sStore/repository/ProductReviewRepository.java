package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>{
    
}
