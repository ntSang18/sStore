package com.pproject.sStore.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "Cart")
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<ProductItem> items = new ArrayList<ProductItem>();

	@JsonIgnore
	@OneToOne(mappedBy = "cart")
	private User user;

	public Cart() {
		super();
	}

	public Cart(User user) {
		super();
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public List<ProductItem> getItems() {
		return items;
	}

	public void setItems(List<ProductItem> items) {
		this.items = items;
	}

	public void addItems(ProductItem item) {
		if (this.items.size() == 0) {
			this.items.add(item);
			item.setCart(this);
		} else {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getProduct().getId() == item.getProduct().getId()
						&& items.get(i).getColor().equals(item.getColor())
						&& items.get(i).getSize().equals(item.getSize())) {
					items.get(i).inQuantity(item.getQuantity());
					return;
				}
			}
			this.items.add(item);
			item.setCart(this);
		}
	}

	public void delItems(Long itemId) {
		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).getId() == itemId) {
				this.items.get(i).setCart(null);
				break;
			}
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getSubTotal() {
		Long SubTotal = 0L;
		for (ProductItem item : items) {
			SubTotal += (item.getQuantity() * item.getProduct().getPrice());
		}
		return SubTotal;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", user=" + user.getId() + "]";
	}

}
