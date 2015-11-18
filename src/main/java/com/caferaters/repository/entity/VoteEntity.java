package com.caferaters.repository.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@Table(name="vote")
public class VoteEntity implements Serializable {

	private static final long serialVersionUID = -6098305432061325382L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ip", nullable = false)
	private String ip;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cafe_id", nullable = false)
	private CafeEntity cafe;

	@Column(name = "created_timepoint", nullable = false)
	private Timestamp createdTimepoint;

	@Column(name = "updated_timepoint")
	private Timestamp updatedTimepoint;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public CafeEntity getCafe() {
		return cafe;
	}
	public void setCafe(CafeEntity cafe) {
		this.cafe = cafe;
	}
	public Timestamp getCreatedTimepoint() {
		return createdTimepoint;
	}
	public void setCreatedTimepoint(Timestamp createdTimepoint) {
		this.createdTimepoint = createdTimepoint;
	}
	public Timestamp getUpdatedTimepoint() {
		return updatedTimepoint;
	}
	public void setUpdatedTimepoint(Timestamp updatedTimepoint) {
		this.updatedTimepoint = updatedTimepoint;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
