package com.challenge.weatherapp.domain;

import java.util.List;

public class ForecastDaily {

	private City city;
	private List<ForecastDailyListItem> list;


	public List<ForecastDailyListItem> getList() {
		return list;
	}

	public void setList(List<ForecastDailyListItem> list) {
		this.list = list;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

}
