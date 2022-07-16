package com.pproject.sStore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "Address")
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String province;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String district;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String ward;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String detailAddress;

	@OneToOne(mappedBy = "address")
	private User user;

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(String province, String district, String commune, String detailAddress) {
		super();
		this.province = province;
		this.district = district;
		this.ward = commune;
		this.detailAddress = detailAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", province=" + province + ", district=" + district + ", commune=" + ward
				+ ", detailAddress=" + detailAddress + "]";
	}

}
