package com.pproject.sStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pproject.sStore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.adminVisible > 0")
    List<Order> getAdminOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 2 " +
            "AND (lower(o.user.userName) LIKE lower(concat('%', ?1,'%')) " +
            "OR o.user.phoneNumber LIKE ?1)")
    Page<Order> getShipperOrders1(String search, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.shipper.id = ?1")
    Page<Order> getShipperOrders2(Long s_id, Pageable pageable);
}
