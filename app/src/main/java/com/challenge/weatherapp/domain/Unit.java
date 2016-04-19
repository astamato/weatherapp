package com.challenge.weatherapp.domain;


import android.util.Log;

public enum Unit {

	IMPERIAL("imperial", "°F"),
	METRIC("metric", "°C");

	private String name;
	private String dispUnit;

	public String getDispUnit() {
		return dispUnit;
	}

	public void setDispUnit(String dispUnit) {
		this.dispUnit = dispUnit;
	}

	public void setName(String name) {
		this.name = name;
	}

	Unit(String name, String dispUnit) {
		this.name = name;
		this.dispUnit = dispUnit;

	}

	public String getName() {
		return name;
	}

	public static Unit findByName(String name) {
		Unit unit = null;
		for (Unit each : values()) {
			if (each.name.equalsIgnoreCase(name)) {
				unit = each;
				break;
			}
		}
		if (unit == null) {
			Log.d("Unit", "Couldnt find unit");
		}
		return unit;
	}

}

