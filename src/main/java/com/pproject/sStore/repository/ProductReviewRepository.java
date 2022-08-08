package com.pproject.sStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pproject.sStore.model.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>{
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.product.id = ?1")
    Page<ProductReview> getPageProductReview(Long product_id, Pageable pageable);
    
    @Query("SELECT pr FROM ProductReview pr WHERE pr.product.id = ?1")
    List<ProductReview> getListProductReview(Long product_id);
}
