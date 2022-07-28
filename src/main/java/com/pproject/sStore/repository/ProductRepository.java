package com.pproject.sStore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pproject.sStore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE lower(p.productName) LIKE lower(concat('%', ?1,'%')) "
            + "OR lower(p.branch) LIKE lower(concat('%', ?1,'%'))")
    Page<Product> search(String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.gender = ?1 "
            + "AND (lower(p.productName) LIKE lower(concat('%', ?2,'%')) "
            + "OR lower(p.branch) LIKE lower(concat('%', ?2,'%')))")
    Page<Product> filterByGender(Boolean gender, String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.gender = ?1 "
            + "AND p.type = ?2 "
            + "AND (lower(p.productName) LIKE lower(concat('%', ?3,'%')) "
            + "OR lower(p.productName) LIKE lower(concat('%', ?3,'%')))")
    Page<Product> filterByType(Boolean gender, String type, String search, Pageable pageable);
}
