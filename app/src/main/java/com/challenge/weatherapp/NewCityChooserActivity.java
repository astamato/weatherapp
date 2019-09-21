package com.challenge.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.challenge.weatherapp.domain.City;
import com.challenge.weatherapp.utils.AppMessageDisplayer;
import com.challenge.weatherapp.utils.CitiesProvider;

import java.util.List;

public class NewCityChooserActivity extends AppCompatActivity {

    private City selectedCity;
    private AutoCompleteTextView mAutoCompleteTextView;
    private CitiesCustomAutocompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city_chooser);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mAutoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        LoadCSVCitiesFile l = new LoadCSVCitiesFile(this);
        l.execute();

        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                if (parent.getItemAtPosition(position) instanceof City) {
                    selectedCity = (City) parent.getItemAtPosition(position);
                }
            }
        });

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selectedCity != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(WeatherAppMainActivity.CITY_NAME_PARAM, selectedCity);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    AppMessageDisplayer.handleSnackBarMessage(findViewById(android.R.id.content),
                            getResources().getString(R.string.problemWithCity)
                            , getResources().getString(R.string.dismiss));

                }
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


    private class LoadCSVCitiesFile extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private List<City> cities;

        public LoadCSVCitiesFile(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            cities = CitiesProvider.getCities(mContext);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    mAdapter = new CitiesCustomAutocompleteAdapter(mContext, R.layout.activity_new_city_chooser,
                            R.id.autoCompleteTextView, cities);
                    mAutoCompleteTextView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }
            });
        }
    }

}