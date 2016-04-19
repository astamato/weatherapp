package com.challenge.weatherapp.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class AppMessageDisplayer {

	public static void handleSnackBarMessage(View v, String message, String actionTitle) {
		final Snackbar snackBar = Snackbar.make(v, message,
				Snackbar.LENGTH_LONG);
		snackBar.setAction(actionTitle, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snackBar.dismiss();
			}
		});
		snackBar.show();
	}

}