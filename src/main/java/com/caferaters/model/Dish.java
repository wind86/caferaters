package com.caferaters.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Dish {

	private Long id;
	private Long cafeId;
	private String name;
	private Cafe cafe;
	private BigDecimal price;
	private Date createdDate;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCafeId() {
		return cafeId;
	}
	public void setCafeId(Long cafeId) {
		this.cafeId = cafeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Cafe getCafe() {
		return cafe;
	}
	public void setCafe(Cafe cafe) {
		this.cafe = cafe;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
