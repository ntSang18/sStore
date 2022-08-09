package com.pproject.sStore.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "Order")
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(nullable = false)
	private LocalDate createAt;

	@Column(nullable = false)
	private Integer totalQuantity;

	@Column(nullable = false)
	private Long totalPrice;

	@Column(nullable = false)
	private int status;

	@Column(nullable = false)
	private int adminVisible;

	@OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductItem> items = new ArrayList<ProductItem>();

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "shipper_id")
	private User shipper;

	public Order() {
		super();
	}

	public Order(LocalDate createAt, int status, User user) {
		super();
		this.createAt = createAt;
		this.totalQuantity = 0;
		this.totalPrice = 0L;
		this.status = status;
		this.adminVisible = 1;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAdminVisible() {
		return adminVisible;
	}

	public void setAdminVisible(int adminVisible) {
		this.adminVisible = adminVisible;
	}

	public List<ProductItem> getItems() {
		return items;
	}

	public void setItems(List<ProductItem> items) {
		this.items = items;
	}

	public void addItem(ProductItem item) {
		this.items.add(item);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getShipper() {
		return shipper;
	}

	public void setShipper(User shipper) {
		this.shipper = shipper;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", createAt=" + createAt + ", status=" + status + ", user=" + user.getId() + "]";
	}

}
