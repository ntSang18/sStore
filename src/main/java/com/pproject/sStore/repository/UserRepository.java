package com.pproject.sStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pproject.sStore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u")
	List<User> getAllUser();

	@Query("SELECT u FROM User u WHERE u.type = 1 OR u.type = 3")
	List<User> getAllClientAndShipper();

	@Query("SELECT u FROM User u WHERE u.email = ?1")
	Optional<User> findUserByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.phoneNumber = ?1")
	Optional<User> findUserByPhoneNumber(String phoneNumber);

	@Query("SELECT COUNT(u) FROM User u WHERE u.type = 1")
	Integer countCustomer();
}
