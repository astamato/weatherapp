package com.challenge.weatherapp.utils;

import android.content.Context;
import android.content.res.Resources;

import com.challenge.weatherapp.domain.City;
import com.challenge.weatherapp.R;

import java.io.InputStream;
import java.util.List;

public class CitiesProvider {

	public static final List<City> getCities(Context context) {
		InputStream inputStream = context.getResources().openRawResource(R.raw.city_list);
		CSVFile csvFile = new CSVFile(inputStream);
		return csvFile.read();
	}

	public static final List<City> getCities() {
		InputStream inputStream = Resources.getSystem().openRawResource(R.raw.city_list);
		CSVFile csvFile = new CSVFile(inputStream);
		return csvFile.read();
	}
}
