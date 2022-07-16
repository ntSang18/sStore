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

@Entity(name = "ProductColor")
@Table(name = "product_color")
public class ProductColor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
    private Long id;
	
	@Column(columnDefinition = "TEXT", nullable = false)
    private String color;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "product_id")
    private Product product;
    
	public ProductColor() {
		super();
	}
	
	public ProductColor(String color, Product product) {
		super();
		this.color = color;
		this.product = product;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductColor [id=" + id + ", color=" + color + ", product=" + product.getId() + "]";
	}
    
    
}
