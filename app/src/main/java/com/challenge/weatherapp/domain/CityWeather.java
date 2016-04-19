package com.challenge.weatherapp.domain;

import java.io.Serializable;
import java.util.List;

public class CityWeather implements Serializable {

	private String cityId;
	private List<Weather> weather;
	private Weather firstWeather;
	private String dt;
	private String name;
	private Sys sys;
	private Main main;

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

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

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sys getSys() {
		return sys;
	}

	public void setSys(Sys sys) {
		this.sys = sys;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}
