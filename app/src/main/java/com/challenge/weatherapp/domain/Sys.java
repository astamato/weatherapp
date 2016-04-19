package com.challenge.weatherapp.domain;

import java.io.Serializable;

public class Sys implements Serializable {

	private Integer id;
	private String country;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
