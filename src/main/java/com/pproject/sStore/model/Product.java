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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Product")
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String productName;

	@Column(nullable = false)
	private Long price;

	@Column(columnDefinition = "TEXT", length = 1000)
	private String description;

	@Column(columnDefinition = "TEXT")
	private String branch;

	@Column(nullable = false)
	private LocalDate createAt;

	// Number product available
	private Long available;

	// Number product sold
	private Long sold;

	// Product for male or for female
	private boolean gender;

	@Column(columnDefinition = "TEXT")
	private String type;

	@OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductImage> images = new ArrayList<ProductImage>();

	@OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductSize> sizes = new ArrayList<ProductSize>();

	@OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductColor> colors = new ArrayList<ProductColor>();

	@OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductReview> reviews = new ArrayList<ProductReview>();

	@OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ProductItem> productItems = new ArrayList<ProductItem>();

	public Product() {
		super();
	}

	public Product(String productName, Long price, String description, String branch, LocalDate createAt,
			Long available, Long sold,
			boolean gender, String type) {
		super();
		this.productName = productName;
		this.price = price;
		this.description = description;
		this.branch = branch;
		this.createAt = createAt;
		this.available = available;
		this.sold = sold;
		this.gender = gender;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public LocalDate getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}

	public Long getAvailable() {
		return available;
	}

	public void setAvailable(Long available) {
		this.available = available;
	}

	public Long getSold() {
		return sold;
	}

	public void setSold(Long sold) {
		this.sold = sold;
	}

	public void inSold(Long sold) {
		this.sold += sold;
		this.available -= sold;
	}

	public void deSold(Long sold) {
		this.sold -= sold;
		this.available += sold;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public void addImage(ProductImage image) {
		if (!this.images.contains(image)) {
			this.images.add(image);
			image.setProduct(this);
		}
	}

	public String getMainImage() {
		return "/static/uploads/product_images/" + id + "/" + images.get(0).getImageName();
	}

	public List<ProductSize> getSizes() {
		return sizes;
	}

	public void setSizes(List<ProductSize> sizes) {
		this.sizes = sizes;
	}

	public void addSize(ProductSize size) {
		if (!this.sizes.contains(size)) {
			this.sizes.add(size);
			size.setProduct(this);
		}
	}

	public List<ProductColor> getColors() {
		return colors;
	}

	public void setColors(List<ProductColor> colors) {
		this.colors = colors;
	}

	public void addColor(ProductColor color) {
		if (!this.colors.contains(color)) {
			this.colors.add(color);
			color.setProduct(this);
		}
	}

	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}

	public void addReview(ProductReview review) {
		if (!this.reviews.contains(review)) {
			this.reviews.add(review);
			review.setProduct(this);
		}
	}

	public List<ProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(List<ProductItem> productItems) {
		this.productItems = productItems;
	}

	@Override
	public String toString() {
		return "Product [available=" + available + ", branch=" + branch + ", createAt=" + createAt + ", description="
				+ description + ", gender=" + gender + ", id=" + id + ", price=" + price + ", productName="
				+ productName + ", sold=" + sold + ", type=" + type + "]";
	}
}
