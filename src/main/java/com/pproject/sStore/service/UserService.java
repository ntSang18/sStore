package com.pproject.sStore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pproject.sStore.model.Address;
import com.pproject.sStore.model.Cart;
import com.pproject.sStore.model.ProductItem;
import com.pproject.sStore.model.User;
import com.pproject.sStore.repository.AddressRepository;
import com.pproject.sStore.repository.CartRepository;
import com.pproject.sStore.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final CartRepository cartRepository;

	@Autowired
	public UserService(UserRepository userRepository, AddressRepository addressRepository,
			CartRepository cartRepository) {
		super();
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
		this.cartRepository = cartRepository;
	}

	public User register(User user, Address address) {
		if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
			throw new IllegalStateException("Email taken");
		}
		if (userRepository.findUserByPhoneNumber(user.getPhoneNumber()).isPresent()) {
			throw new IllegalStateException("Phone number taken");
		}
		addressRepository.save(address);
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setAddress(address);
		user.setCart(cart);
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
