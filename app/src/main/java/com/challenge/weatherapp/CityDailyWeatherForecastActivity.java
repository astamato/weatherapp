package com.challenge.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.challenge.weatherapp.domain.CityWeather;
import com.challenge.weatherapp.domain.ForecastDaily;
import com.challenge.weatherapp.service.OpenWeatherApiService;
import com.challenge.weatherapp.service.OpenWeatherConnector;
import com.challenge.weatherapp.utils.AppMessageDisplayer;
import com.challenge.weatherapp.utils.CityWeatherInfoProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityDailyWeatherForecastActivity extends AppCompatActivity {

    private OpenWeatherApiService service = OpenWeatherApiService.retrofit.create(OpenWeatherApiService.class);
    private static final Integer DAY_COUNT = 5;
    private RecyclerView mRecyclerView;
    private CityDailyWeatherForecastListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CityWeatherInfoProgressDialog mProgressDialog;
    private CityWeather citySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_forecast_details);
        citySelected = (CityWeather) getIntent().getSerializableExtra(WeatherAppMainActivity.CITY_SELECTED);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(citySelected.getName() + ", " + citySelected.getSys().getCountry());
        }
        mProgressDialog = new CityWeatherInfoProgressDialog(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.cityForecastRecycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getDailyWeatherForecast(citySelected.getCityId(), Boolean.TRUE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.cityForecastSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

    }

    void refreshItems() {
        getDailyWeatherForecast(citySelected.getCityId(), Boolean.FALSE);
    }

    void onItemsLoadComplete() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getDailyWeatherForecast(String cityId, boolean shouldShowLoadingProgressDialog) {
        if (shouldShowLoadingProgressDialog) {
            mProgressDialog.showProgressDialog();
        }
        Call<ForecastDaily> call = service.getForecastDaily(OpenWeatherConnector.API_KEY,
                WeatherAppMainActivity.UNIT.getName(), DAY_COUNT, cityId);
        call.enqueue(new Callback<ForecastDaily>() {
            @Override
            public void onResponse(Call<ForecastDaily> call, final Response<ForecastDaily> response) {
                if (response != null && response.body() != null) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mAdapter = new CityDailyWeatherForecastListAdapter(response.body().getList(), CityDailyWeatherForecastActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.invalidate();
                        }
                    });
                }
                onItemsLoadComplete();
                mProgressDialog.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ForecastDaily> call, Throwable t) {
                Log.d("WeatherAppMainActivity", "Communication Error", t);
                AppMessageDisplayer.handleSnackBarMessage(findViewById(R.id.cityForecastRecycler),
                        "Communication Error", "dismiss");
                onItemsLoadComplete();
                mProgressDialog.hideProgressDialog();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
