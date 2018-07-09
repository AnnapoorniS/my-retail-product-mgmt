package com.example.productmgmt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "ProductDescription")
public class ProductDescription {

	@Id
	int id;
	String description;

	public ProductDescription(int id, String description) {
		this.id = id;
		this.description = description;
	}

	@JsonProperty("id")
	public int getId() {
		return this.id;
	}

	@JsonProperty("product_desc")
	public String getDescription() {
		return this.description;
	}
}
