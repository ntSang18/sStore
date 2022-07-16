package com.pproject.sStore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pproject.sStore.model.Address;
import com.pproject.sStore.model.User;
import com.pproject.sStore.repository.AddressRepository;
import com.pproject.sStore.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;

	@Autowired
	public UserService(UserRepository userRepository, AddressRepository addressRepository) {
		super();
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
	}

	public User register(User user, Address address) {
		if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
			throw new IllegalStateException("Email taken");
		}
		if (userRepository.findUserByPhoneNumber(user.getPhoneNumber()).isPresent()) {
			throw new IllegalStateException("Phone number taken");
		}
		addressRepository.save(address);
		user.setAddress(address);
		user.setType(1);
		return userRepository.save(user);
	}

	public User login(String email, String password) {
		return userRepository.login(email, password)
				.orElseThrow(() -> new IllegalStateException("email or password incorrect"));
	}
	
	public Integer countCustomer() {
		return userRepository.countCustomer();
	}
}
