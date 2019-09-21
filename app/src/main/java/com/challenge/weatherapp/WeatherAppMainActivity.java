package com.challenge.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.challenge.weatherapp.domain.City;
import com.challenge.weatherapp.domain.CityWeather;
import com.challenge.weatherapp.domain.CityWeatherList;
import com.challenge.weatherapp.domain.Unit;
import com.challenge.weatherapp.service.OpenWeatherApiService;
import com.challenge.weatherapp.service.OpenWeatherConnector;
import com.challenge.weatherapp.storage.StoredCitiesDataSource;
import com.challenge.weatherapp.utils.AppMessageDisplayer;
import com.challenge.weatherapp.utils.CityWeatherInfoProgressDialog;
import com.challenge.weatherapp.utils.StringUtils;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherAppMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CityListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StoredCitiesDataSource storedCitiesDataSource;
    private List<CityWeather> cityWeatherDataSet = new ArrayList<>();
    private CityWeatherInfoProgressDialog mProgressDialog;

    private OpenWeatherApiService service = OpenWeatherApiService.retrofit.create(OpenWeatherApiService.class);

    public static final Unit UNIT = Unit.IMPERIAL;

    public static final int NEW_CITY_RESULT = 1;
    public static final String CITY_SELECTED = "CITY_SELECTED";
    public static final String CITY_NAME_PARAM = "CITY_NAME_PARAM";


    private String[] getStoredCities() {
        return storedCitiesDataSource.getAllIds();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_app_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storedCitiesDataSource = new StoredCitiesDataSource(this);
        mProgressDialog = new CityWeatherInfoProgressDialog(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                onDismissBySwype(reverseSortedPositions);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                onDismissBySwype(reverseSortedPositions);
                            }
                        });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

        getWeatherInfoCityList(getStoredCities(), Boolean.TRUE);


        final FloatingActionButton fab = findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(WeatherAppMainActivity.this, NewCityChooserActivity.class);
                    startActivityForResult(i, WeatherAppMainActivity.NEW_CITY_RESULT);
                }
            });
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown())
                    fab.hide();
                else if (dy < 0 && !fab.isShown())
                    fab.show();
            }
        });


        mSwipeRefreshLayout = findViewById(R.id.cityForecastSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

    }

    void refreshItems() {
        cityWeatherDataSet.clear();
        getWeatherInfoCityList(getStoredCities(), Boolean.FALSE);
    }

    void onItemsLoadComplete() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onDismissBySwype(int[] reverseSortedPositions) {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!fab.isShown()) {
            fab.show();
        }
        for (int position : reverseSortedPositions) {
            final String cityId = cityWeatherDataSet.get(position).getCityId();
            final String cityName = cityWeatherDataSet.get(position).getName();
            storedCitiesDataSource.removeCityById(cityId);
            cityWeatherDataSet.remove(position);
            mAdapter.notifyItemRemoved(position);
            Snackbar.make(findViewById(R.id.mainRecyclerView), getResources().getString(R.string.removedCity) +
                    cityName, Snackbar.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateRecyclerViewData() {
        mAdapter = new CityListAdapter(cityWeatherDataSet, new CityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CityWeather item) {
                Intent myIntent = new Intent(WeatherAppMainActivity.this, CityDailyWeatherForecastActivity.class);
                myIntent.putExtra(CITY_SELECTED, item);
                WeatherAppMainActivity.this.startActivity(myIntent);
            }
        }, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.invalidate();
        onItemsLoadComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_CITY_RESULT) {
            if (resultCode == RESULT_OK) {
                City c = (City) data.getSerializableExtra(CITY_NAME_PARAM);
                if (c != null) {
                    if (checkCityIsNew(c)) {
                        storedCitiesDataSource.addCity(c);
                        getWeatherInfoSingleCity(cityWeatherDataSet, c);
                    } else {
                        AppMessageDisplayer.handleSnackBarMessage(findViewById(android.R.id.content),
                                getResources().getString(R.string.alreadyStoredCity) + c.getName() +
                                        ", " + c.getCountryCode(), getResources().getString(R.string.dismiss));
                    }
                } else {
                    AppMessageDisplayer.handleSnackBarMessage(findViewById(android.R.id.content),
                            getResources().getString(R.string.problemWithCity)
                            , getResources().getString(R.string.dismiss));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkCityIsNew(City newCity) {
        for (String c : getStoredCities()) {
            if (c.equals(newCity.getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_app_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.changeUnit) {
            AppMessageDisplayer.handleSnackBarMessage(findViewById(android.R.id.content),
                    getResources().getString(R.string.changeUnitComingSoon), getResources().getString(R.string.dismiss));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getWeatherInfoCityList(final String[] storedCities, boolean shouldShowLoadingProgressDialog) {
        if (shouldShowLoadingProgressDialog) {
            mProgressDialog.showProgressDialog();
        }
        Call<CityWeatherList> call = service.getWeatherSeveralCities(OpenWeatherConnector.API_KEY, UNIT.getName(),
                StringUtils.commaSeparatedString(storedCities));
        call.enqueue(new Callback<CityWeatherList>() {
            @Override
            public void onResponse(Call<CityWeatherList> call, Response<CityWeatherList> response) {
                if (response != null && response.body() != null && response.body().getList() != null) {
                    int i = 0;
                    for (CityWeather c : response.body().getList()) {
                        c.setCityId(storedCities[i]);
                        cityWeatherDataSet.add(c);
                        i++;
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            updateRecyclerViewData();
                        }
                    });
                } else {
                    AppMessageDisplayer.handleSnackBarMessage(findViewById(R.id.mainRecyclerView),
                            getResources().getString(R.string.errorGettingCities), getResources().getString(R.string.dismiss));
                }
                mProgressDialog.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<CityWeatherList> call, Throwable t) {
                Log.d("WeatherAppMainActivity", "Communication Error", t);
                AppMessageDisplayer.handleSnackBarMessage(findViewById(R.id.mainRecyclerView), "Communication Error",
                        getResources().getString(R.string.dismiss));
                mProgressDialog.hideProgressDialog();
                onItemsLoadComplete();
            }

        });
    }

    private void getWeatherInfoSingleCity(final List<CityWeather> cityWeatherDataSet, final City s) {
        mProgressDialog.showProgressDialog();
        Call<CityWeather> call = service.getWeather(OpenWeatherConnector.API_KEY, UNIT.getName(), s.getId());
        call.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                if (response != null && response.body() != null && response.body().getName() != null) {
                    CityWeather c = response.body();
                    c.setCityId(s.getId());
                    cityWeatherDataSet.add(c);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            updateRecyclerViewData();
                        }
                    });
                    mProgressDialog.hideProgressDialog();
                } else {
                    AppMessageDisplayer.handleSnackBarMessage(findViewById(R.id.mainRecyclerView),
                            getString(R.string.troubleLoading) + s.getName(),
                            getResources().getString(R.string.dismiss));
                    mProgressDialog.hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Log.d("WeatherAppMainActivity", "Communication Error", t);
                AppMessageDisplayer.handleSnackBarMessage(findViewById(R.id.mainRecyclerView), "Communication Error", "dismiss");
                mProgressDialog.hideProgressDialog();
            }
        });
    }

}
