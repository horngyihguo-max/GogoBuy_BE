package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_option_groups")
//@IdClass(value = ProductOptionGroupsId.class)
public class ProductOptionGroups {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

//	@Id
	@Column(name = "stores_id")
	private int storesId;
	
	@Column(name = "name")
	private String name;

	@Column(name = "is_required")
	private boolean required;

	@Column(name = "max_selection")
	private Integer maxSelection;

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
		return maxSelection;
	}

	public void setMaxSelection(Integer maxSelection) {
		this.maxSelection = maxSelection;
	}
	
	
}
