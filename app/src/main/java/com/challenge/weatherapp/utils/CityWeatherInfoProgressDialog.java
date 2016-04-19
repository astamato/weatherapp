package com.challenge.weatherapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.challenge.weatherapp.R;

public class CityWeatherInfoProgressDialog extends ProgressDialog {

	private Context mContext;

	public CityWeatherInfoProgressDialog(Context context) {
		super(context);
		mContext = context;
	}

	public void showProgressDialog(){
		this.setMessage(mContext.getResources().getString(R.string.progressBarLoading));
		this.setCancelable(false);
		this.setInverseBackgroundForced(false);
		this.show();
	}

	public void hideProgressDialog(){
		this.hide();
	}
}
