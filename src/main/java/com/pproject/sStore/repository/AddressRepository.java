package com.pproject.sStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pproject.sStore.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
