package com.caferaters.repository.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@Table(name="dish")
public class DishEntity implements Serializable {

	private static final long serialVersionUID = -7638793673723321840L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cafe_id", nullable = false)
	private CafeEntity cafe;

	@Column(name = "price", nullable = false, precision = 2)
	private BigDecimal price;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CafeEntity getCafe() {
		return cafe;
	}
	public void setCafe(CafeEntity cafe) {
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