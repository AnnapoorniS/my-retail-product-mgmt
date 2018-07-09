package com.example.productmgmt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

	int id;
	String description;
	Price current_price;

	public Product(int id, String description, Price current_price) {
		this.id = id;
		this.description = description;
		this.current_price = current_price;
	}

	@JsonProperty("id")
	public int getId() {
		return this.id;
	}

	@JsonProperty("product_desc")
	public String getDescription() {
		return this.description;
	}

	@JsonProperty("current_price")
	public Price getPrice() {
		return this.current_price;
	}

}
