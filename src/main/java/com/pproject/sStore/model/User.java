package com.pproject.sStore.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

	@Column(nullable = false)
	private int type;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

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

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", phoneNumber=" + phoneNumber + ", type=" + type + "]";
	}

}