package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_option_groups")
@IdClass(value = ProductOptionGroupsId.class)
public class ProductOptionGroups {

	@Id
	@Column(name = "id")
	private int id;

	@Id
	@Column(name = "stores_id")
	private int storesId;
	
	@Column(name = "name")
	private String name;

	@Column(name = "required")
	private boolean required;

	@Column(name = "max_election")
	private Integer maxElection;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoresId() {
		return storesId;
	}

	public void setStoresId(int storesId) {
		this.storesId = storesId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMaxElection() {
		return maxElection;
	}

	public void setMaxElection(Integer maxElection) {
		this.maxElection = maxElection;
	}
	
	
}
