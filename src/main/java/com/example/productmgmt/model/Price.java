package com.example.productmgmt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Price {
	
	double value;
	String currency_code;

	public Price(double value, String currency_code) {
		this.value = value;
		this.currency_code = currency_code;
	}

	@JsonProperty("value")
	public double getValue() {
		return this.value;
	}

	@JsonProperty("currency_code")
	public String getCurrencyCode() {
		return this.currency_code;
	}
}
