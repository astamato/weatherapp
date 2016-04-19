package com.challenge.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.challenge.weatherapp.domain.CityWeather;
import com.challenge.weatherapp.domain.Weather;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

	public interface OnItemClickListener {
		void onItemClick(CityWeather item);
	}

	private List<CityWeather> mDataset;
	private final OnItemClickListener mListener;
	private final Context mContext;

	public static final String IMAGE_URL_BASE = "http://openweathermap.org/img/w/";
	public static final String WEATHER_IMG_EXTENSION= ".png";


	public List<CityWeather> getmDataset() {
		return mDataset;
	}

	public void setmDataset(List<CityWeather> mDataset) {
		this.mDataset = mDataset;
	}

	public CityListAdapter(List<CityWeather> myDataset, OnItemClickListener listener, Context context) {
		super();
		mDataset = myDataset;
		mListener = listener;
		mContext = context;
	}

	@Override
	public CityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.city_item, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		CityWeather c = mDataset.get(position);
		String dispUnit = WeatherAppMainActivity.UNIT.getDispUnit();
		Weather w = mDataset.get(position).getFirstWeather();

		if (c != null) {
			holder.mCityName.setText(c.getName() + ", " + c.getSys().getCountry());
			holder.mWeatherMain.setText(w.getMain());
			holder.mTemp.setText(c.getMain().getTemp() + " " + dispUnit);
			holder.mTempMax.setText(c.getMain().getTempMax() + " " + dispUnit);
			holder.mTempMin.setText(c.getMain().getTempMin() + " " + dispUnit);
			holder.mPressure.setText(c.getMain().getPressure() + " hPa");
			holder.mHumidity.setText(c.getMain().getHumidity() + " %");

			Picasso.with(mContext).load(IMAGE_URL_BASE + w.getIcon() + WEATHER_IMG_EXTENSION).into(holder.mImageView);

		}

		holder.bind(mDataset.get(position), mListener);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView mImageView;
		public TextView mCityName;
		public TextView mTemp;
		public TextView mTempMax;
		public TextView mTempMin;
		public TextView mPressure;
		public TextView mHumidity;
		public TextView mWeatherMain;

		public ViewHolder(View itemView) {
			super(itemView);
			mImageView = (ImageView)itemView.findViewById(R.id.weatherPic);
			mCityName = (TextView)itemView.findViewById(R.id.cityName);
			mWeatherMain = (TextView)itemView.findViewById(R.id.weatherMain);
			mTemp = (TextView)itemView.findViewById(R.id.temp);
			mTempMax = (TextView)itemView.findViewById(R.id.tempMax);
			mTempMin = (TextView)itemView.findViewById(R.id.tempMin);
			mPressure = (TextView)itemView.findViewById(R.id.pressure);
			mHumidity = (TextView)itemView.findViewById(R.id.humidity);
		}

		public void bind(final CityWeather item, final OnItemClickListener listener) {
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onItemClick(item);
				}
			});
		}

	}

}
