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

@Entity(name = "ProductSize")
@Table(name = "product_size")
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String size;
    
    @JsonIgnore
    @ManyToOne
	@JoinColumn(name = "product_id")
    private Product product;

	public ProductSize() {
		super();
	}

	public ProductSize(String size, Product product) {
		super();
		this.size = size;
		this.product = product;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductSize [id=" + id + ", size=" + size + ", product=" + product.getId() + "]";
	}
    
}
