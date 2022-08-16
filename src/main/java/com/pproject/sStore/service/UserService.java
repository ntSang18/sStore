package com.pproject.sStore.service;

import com.pproject.sStore.model.Address;
import com.pproject.sStore.model.Cart;
import com.pproject.sStore.model.User;
import com.pproject.sStore.repository.AddressRepository;
import com.pproject.sStore.repository.CartRepository;
import com.pproject.sStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final CartRepository cartRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, AddressRepository addressRepository,
			CartRepository cartRepository) {
		super();
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
		this.cartRepository = cartRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}

	public List<User> getAllClientAndShipper() {
		return userRepository.getAllClientAndShipper();
	}

	public List<User> getListUser(String search) {
		List<User> customers = new ArrayList<>();
		if (search.trim().equals("")) {
			customers = getAllClientAndShipper();
		} else {
			for (User user : getAllClientAndShipper()) {
				if (user.getUserName().toLowerCase().contains(search.toLowerCase()) ||
						user.getEmail().toLowerCase().contains(search.toLowerCase()) ||
						user.getPhoneNumber().contains(search)) {
					customers.add(user);
				}
			}
		}
		return customers;
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("User by id " + id + "does not exits"));
	}

	public User register(User user, Address address) {
		if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
			throw new IllegalStateException("Email taken");
		}
		if (userRepository.findUserByPhoneNumber(user.getPhoneNumber()).isPresent()) {
			throw new IllegalStateException("Phone number taken");
		}
		String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		addressRepository.save(address);
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setAddress(address);
		user.setCart(cart);
		if (user.getType() == 0) {
			user.setType(1);
		}
		return userRepository.save(user);
	}

	public User login(String email, String password) {
		for (User u : getAllUser()) {
			if (u.getEmail().trim().equals(email.trim()) &&
					this.passwordEncoder.matches(password, u.getPassword())) {
				return u;
			}
		}
		throw new IllegalStateException("email or password incorrect");
	}

	public User editUser(User user, Address address, Long uid) {
		User oldUser = userRepository.findById(uid)
				.orElseThrow(() -> new IllegalStateException("User doest not exits"));
		if (!user.getEmail().trim().equals(oldUser.getEmail().trim())) {
			if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
				throw new IllegalStateException("Email taken");
			}
		}
		if (!user.getPhoneNumber().trim().equals(oldUser.getPhoneNumber().trim())) {
			if (userRepository.findUserByPhoneNumber(user.getPhoneNumber()).isPresent()) {
				throw new IllegalStateException("Phone number taken");
			}
		}
		Address oldAddress = oldUser.getAddress();
		oldAddress.copy(address);
		addressRepository.save(oldAddress);
		oldUser.copy(user);
		return userRepository.save(oldUser);
	}

	public User changeEmail(User user, String newEmail) {
		if (!user.getEmail().trim().equals(newEmail.trim())) {
			if (userRepository.findUserByEmail(newEmail.trim()).isPresent()) {
				throw new IllegalStateException("Email taken");
			}
		}
		user.setEmail(newEmail);
		return userRepository.save(user);
	}

	public User changePhone(User user, String newPhoneNumber) {
		if (!user.getPhoneNumber().trim().equals(newPhoneNumber.trim())) {
			if (userRepository.findUserByPhoneNumber(newPhoneNumber.trim()).isPresent()) {
				throw new IllegalStateException("Phone number taken");
			}
		}
		user.setPhoneNumber(newPhoneNumber);
		return userRepository.save(user);
	}

	public User changeAddress(User user, Address address) {
		Address oldAddress = user.getAddress();
		oldAddress.copy(address);
		addressRepository.save(oldAddress);
		return userRepository.save(user);
	}

	public User changePassword(User user, String currentPass, String newPass) {
		String oldPass = user.getPassword();
		if (!this.passwordEncoder.matches(currentPass, oldPass)) {
			System.out.println("not matches");
			throw new IllegalStateException("Incorrect current password");
		}
		String newEncodedPassword = this.passwordEncoder.encode(newPass);
		user.setPassword(newEncodedPassword);
		return userRepository.save(user);
	}

	public Integer countCustomer() {
		return userRepository.countCustomer();
	}
}
