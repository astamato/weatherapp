package com.challenge.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.challenge.weatherapp.domain.ForecastDailyListItem;
import com.challenge.weatherapp.domain.ForecastDailyTemp;
import com.challenge.weatherapp.domain.Weather;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CityDailyWeatherForecastListAdapter extends RecyclerView.Adapter<CityDailyWeatherForecastListAdapter.ViewHolder> {

    private List<ForecastDailyListItem> mDataset;
    private final Context mContext;

    public static final String WEATHER_DATE_FORMAT = "EEE, dd MMM yyyy";
    public static final String IMAGE_URL_BASE = "http://openweathermap.org/img/w/";
    public static final String WEATHER_IMG_EXTENSION = ".png";


    public CityDailyWeatherForecastListAdapter(List<ForecastDailyListItem> mDataset, Context context) {
        super();
        this.mDataset = mDataset;
        this.mContext = context;
    }

    @Override
    public CityDailyWeatherForecastListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_forecast_details_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CityDailyWeatherForecastListAdapter.ViewHolder holder, int position) {
        ForecastDailyTemp c = mDataset.get(position).getTemp();
        Date date = new Date(mDataset.get(position).getDt() * 1000);
        Weather w = mDataset.get(position).getFirstWeather();

        DateFormat formatter = new SimpleDateFormat(WEATHER_DATE_FORMAT);
        String dateFormatted = formatter.format(date);

        String dispUnit = WeatherAppMainActivity.UNIT.getDispUnit();
        if (c != null) {
            holder.mDateTime.setText(dateFormatted);
            holder.mWeatherMain.setText(w.getMain());
            holder.mDay.setText(c.getDay() + " " + dispUnit);
            holder.mMin.setText(c.getMin() + " " + dispUnit);
            holder.mMax.setText(c.getMax() + " " + dispUnit);
            Picasso.with(mContext).load(IMAGE_URL_BASE + w.getIcon() + WEATHER_IMG_EXTENSION).into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mDateTime;
        public TextView mDay;
        public TextView mMin;
        public TextView mMax;
        public TextView mWeatherMain;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.weatherPicDetails);
            mWeatherMain = (TextView) itemView.findViewById(R.id.weatherMain);
            mDateTime = (TextView) itemView.findViewById(R.id.dateTime);
            mDay = (TextView) itemView.findViewById(R.id.day);
            mMin = (TextView) itemView.findViewById(R.id.min);
            mMax = (TextView) itemView.findViewById(R.id.max);

        }

    }

}
