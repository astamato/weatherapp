package com.challenge.weatherapp.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.challenge.weatherapp.domain.City;
import com.challenge.weatherapp.domain.StoredCitiesDTO;

import java.util.ArrayList;
import java.util.List;

public class StoredCitiesDataSource {

	private WeatherAppResourcesDbHelper dbHelper;

	public StoredCitiesDataSource(Context context) {
		this.dbHelper = new WeatherAppResourcesDbHelper(context);
	}

	protected WeatherAppResourcesDbHelper getDbHelper() {
		return dbHelper;
	}

	public List<StoredCitiesDTO> getAll() {
		List<StoredCitiesDTO> contacts = new ArrayList<StoredCitiesDTO>();
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ WeatherAppResourcesDbHelper.DEFAULT_CITIES_TABLE, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoredCitiesDTO contact = cursorToCity(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return contacts;
	}

	public String[] getAllIds() {
		List<String> citiIds = new ArrayList<String>();
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT " + WeatherAppResourcesDbHelper.DEFAULT_CITIES_CITY_ID
				+ " FROM " + WeatherAppResourcesDbHelper.DEFAULT_CITIES_TABLE, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			citiIds.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return citiIds.toArray(new String[citiIds.size()]);
	}


	public boolean removeCityById(String id) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		return database.delete(WeatherAppResourcesDbHelper.DEFAULT_CITIES_TABLE,
			WeatherAppResourcesDbHelper.DEFAULT_CITIES_CITY_ID + "='" + id + "'", null) > 0;
	}

	public void addCity(City c) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		String sql = "INSERT INTO " + WeatherAppResourcesDbHelper.DEFAULT_CITIES_TABLE + " (" +
				WeatherAppResourcesDbHelper.DEFAULT_CITIES_CITY_ID + "," +
				WeatherAppResourcesDbHelper.DEFAULT_CITIES_CITY_NAME + "," +
				WeatherAppResourcesDbHelper.DEFAULT_CITIES_COUNTRY_CODE + ") " +
				"VALUES ('" + c.getId() + "', '" + c.getName() + "', '"+ c.getCountryCode()+"')";
		database.execSQL(sql);
	}

	private StoredCitiesDTO cursorToCity(Cursor cursor) {
		StoredCitiesDTO scenario = new StoredCitiesDTO();
		scenario.setCityId(cursor.getInt(0));
		scenario.setCityDescription(cursor.getString(1));
		return scenario;
	}

}
