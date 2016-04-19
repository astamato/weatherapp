package com.challenge.weatherapp.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class WeatherAppResourcesDbHelper extends SQLiteOpenHelper {

	private final Context context;

	//MAIN DATABASE INFO
	private static final String DATABASE_NAME = "weatherapp.db";
	private static int DATABASE_VERSION = 1;

	//DEFAULT_CITIES TABLE
	public static final String DEFAULT_CITIES_TABLE = "stored_cities";
	public static final String DEFAULT_CITIES_CITY_ID = "city_id";
	public static final String DEFAULT_CITIES_CITY_NAME = "city_description";
	public static final String DEFAULT_CITIES_COUNTRY_CODE = "country_code";


	private static final String DATABASE_CREATE =
			"CREATE TABLE " + DEFAULT_CITIES_TABLE + "("
					+ DEFAULT_CITIES_CITY_ID + " text not null, "
					+ DEFAULT_CITIES_CITY_NAME + " text not null, "
					+ DEFAULT_CITIES_COUNTRY_CODE + " text  "
					+ ");";

	public WeatherAppResourcesDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (checkDataBase()) {
			if (!isTableExists(db, DEFAULT_CITIES_TABLE)) {
				db.execSQL(DATABASE_CREATE);

				//INSERT DEFAULT CITIES
				String sql = "INSERT INTO " + DEFAULT_CITIES_TABLE + " (" + DEFAULT_CITIES_CITY_ID + ", " + DEFAULT_CITIES_CITY_NAME + ") " +
						"VALUES ('6356055','Barcelona,es')";
				db.execSQL(sql);
				sql = "INSERT INTO " + DEFAULT_CITIES_TABLE + " (" + DEFAULT_CITIES_CITY_ID + ", " + DEFAULT_CITIES_CITY_NAME + ") " +
						"VALUES ('2964574','Dublin,ie')";
				db.execSQL(sql);
				sql = "INSERT INTO " + DEFAULT_CITIES_TABLE + " (" + DEFAULT_CITIES_CITY_ID + ", " + DEFAULT_CITIES_CITY_NAME + ") " +
						"VALUES ('5128638','New York,us')";
				db.execSQL(sql);
				sql = "INSERT INTO " + DEFAULT_CITIES_TABLE + " (" + DEFAULT_CITIES_CITY_ID + ", " + DEFAULT_CITIES_CITY_NAME + ") " +
						"VALUES ('2643743','London,uk')";
				db.execSQL(sql);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(WeatherAppResourcesDbHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DEFAULT_CITIES_TABLE);
		DATABASE_VERSION = newVersion;
		onCreate(db);
	}

	boolean isTableExists(SQLiteDatabase db, String tableName) {
		if (tableName == null || db == null || !db.isOpen()) {
			return false;
		}
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
		if (!cursor.moveToFirst()) {
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}

	public boolean checkDataBase() {
		File database = context.getDatabasePath(DATABASE_NAME);
		if (!database.exists()) {
			return false;
		}
		return true;
	}
	
}
