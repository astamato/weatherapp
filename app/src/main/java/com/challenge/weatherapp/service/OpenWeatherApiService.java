package com.challenge.weatherapp.service;

import com.challenge.weatherapp.domain.CityWeather;
import com.challenge.weatherapp.domain.CityWeatherList;
import com.challenge.weatherapp.domain.ForecastDaily;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApiService {

	String BASE_URL = "http://api.openweathermap.org/";

	@GET("data/2.5/forecast/daily")
	Call<ForecastDaily> getForecastDaily(@Query("appid") String appid,
										 @Query("units") String units,
										 @Query("cnt") Integer count,
										 @Query("id") String id
	);


	@GET("data/2.5/weather")
	Call<CityWeather> getWeather(@Query("appid") String appid,
								 @Query("units") String units,
								 @Query("id") String id
	);

	@GET("data/2.5/group")
	Call<CityWeatherList> getWeatherSeveralCities(@Query("appid") String appid,
												  @Query("units") String units,
												  @Query("id") String feedIds
	);


	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();

}
