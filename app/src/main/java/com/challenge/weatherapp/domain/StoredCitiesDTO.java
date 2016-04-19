package com.challenge.weatherapp.domain;

public class StoredCitiesDTO {

	public Integer cityId;
	public String cityDescription;
	public String countryCode;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityDescription() {
		return cityDescription;
	}

	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}


}
