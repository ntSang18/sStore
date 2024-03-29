package com.pproject.sStore.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "User")
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String userName;

	@Column(nullable = false, columnDefinition = "TEXT", unique = true)
	private String email;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String password;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String phoneNumber;

	// type = 1: client
	// type = 2: admin
	// type = 3: shipper
	@Column(nullable = false)
	private int type;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	private Cart cart;

	@JsonBackReference
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();

	@JsonBackReference
	@OneToMany(mappedBy = "shipper", cascade = CascadeType.ALL)
	private List<Order> shipperOrders = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductReview> reviews = new ArrayList<>();

	public User() {
		super();
	}

	public User(String userName, String email, String password, String phoneNumber, int type) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addOrder(Order order) {
		if (!this.orders.contains(order)) {
			this.orders.add(order);
			order.setUser(this);
		}
	}

	public List<Order> getShipperOrders() {
		return shipperOrders;
	}

	public void setShipperOrders(List<Order> shipperOrders) {
		this.shipperOrders = shipperOrders;
	}

	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}

	public void copy(User user) {
		this.userName = user.getUserName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.phoneNumber = user.getPhoneNumber();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", phoneNumber=" + phoneNumber + ", type=" + type + "]";
	}

}
