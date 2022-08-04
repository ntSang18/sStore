package com.pproject.sStore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "ProductItem")
@Table(name = "product_item")
public class ProductItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String size;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String color;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private int status;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonManagedReference
	private Product product;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public ProductItem() {
		super();
	}

	public ProductItem(String size, String color, int quantity, int status, Product product, Cart cart, Order order) {
		super();
		this.size = size;
		this.color = color;
		this.quantity = quantity;
		this.status = status;
		this.product = product;
		this.cart = cart;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void inQuantity(int quantity) {
		this.quantity += quantity;
	}

	public void deQuantity(int quantity) {
		this.quantity -= quantity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "ProductItem [id=" + id + ", size=" + size + ", color=" + color + ", quantity=" + quantity + ", status="
				+ status + ", product=" + product.getId() + "]";
	}

}
