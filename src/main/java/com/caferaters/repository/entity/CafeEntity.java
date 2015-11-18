package com.caferaters.repository.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@Table(name="cafe", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class CafeEntity implements Serializable {

	private static final long serialVersionUID = 545750338694754159L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "name", unique = true, nullable = false)
	private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cafe")
	private Set<DishEntity> dishes = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cafe")
	private Set<VoteEntity> votes = new HashSet<>(0);

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
	public Set<DishEntity> getDishes() {
		return dishes;
	}
	public void setDishes(Set<DishEntity> dishes) {
		this.dishes = dishes;
	}
	public Set<VoteEntity> getVotes() {
		return votes;
	}
	public void setVotes(Set<VoteEntity> votes) {
		this.votes = votes;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
