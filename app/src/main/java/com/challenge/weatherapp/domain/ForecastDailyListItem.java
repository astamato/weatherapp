package com.challenge.weatherapp.domain;

import java.util.List;

public class ForecastDailyListItem {

	private Long dt;
	private ForecastDailyTemp temp;
	private List<Weather> weather;
	private Weather firstWeather;

	public List<Weather> getWeather() {
		return weather;
	}

	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}

	public void setFirstWeather(Weather firstWeather) {
		this.firstWeather = firstWeather;
	}

	public Weather getFirstWeather() {
		return this.weather.get(0);
	}

	public Long getDt() {
		return dt;
	}

	public void setDt(Long dt) {
		this.dt = dt;
	}

	public ForecastDailyTemp getTemp() {
		return temp;
	}

	public void setTemp(ForecastDailyTemp temp) {
		this.temp = temp;
	}
}
