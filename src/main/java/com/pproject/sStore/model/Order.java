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
	private int status;

	@OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductItem> items = new ArrayList<ProductItem>();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Order() {
		super();
	}

	public Order(LocalDate createAt, int status, User user) {
		super();
		this.createAt = createAt;
		this.status = status;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<ProductItem> getItems() {
		return items;
	}

	public void setItems(List<ProductItem> items) {
		this.items = items;
	}

	public void addItems(ProductItem item) {
		if (!this.items.contains(item)) {
			this.addItems(item);
			item.setOrder(this);
		} else {
			this.items.get(this.items.indexOf(item)).inQuantity(item.getQuantity());
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", createAt=" + createAt + ", status=" + status + ", user=" + user.getId() + "]";
	}

}
