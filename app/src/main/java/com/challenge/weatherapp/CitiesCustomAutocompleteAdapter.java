package com.challenge.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.challenge.weatherapp.domain.City;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CitiesCustomAutocompleteAdapter extends ArrayAdapter<City> {

	private List<City> tempItems, suggestions;
	private Context context;

	public CitiesCustomAutocompleteAdapter(Context context, int resource, int textViewResourceId, List<City> items) {
		super(context, resource, textViewResourceId, items);
		this.context = context;
		tempItems = new ArrayList<>(items); // this makes the difference.
		suggestions = new ArrayList<>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_city, parent, false);
		}
		City cities = getItem(position);
		if (cities != null) {
			TextView lblName = (TextView)view.findViewById(R.id.lbl_name);
			if (lblName != null)
				lblName.setText(cities.getName() + ", " + cities.getCountryCode());
		}
		return view;
	}

	@Override
	public Filter getFilter() {
		return nameFilter;
	}

	/**
	 * Custom Filter implementation for custom suggestions we provide.
	 */
	Filter nameFilter = new Filter() {
		@Override
		public CharSequence convertResultToString(Object resultValue) {
			String str = ((City)resultValue).getName() + ", " + ((City)resultValue).getCountryCode();
			return str;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (constraint != null) {
				suggestions.clear();
				String suggestionTarget = null;
				for (City city : tempItems) {
					suggestionTarget = city.getName() + ", " + city.getCountryCode();
					if (suggestionTarget.toLowerCase().contains(constraint.toString().toLowerCase())) {
						suggestions.add(city);
					}
				}
				FilterResults filterResults = new FilterResults();
				filterResults.values = suggestions;
				filterResults.count = suggestions.size();
				return filterResults;
			} else {
				return new FilterResults();
			}
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<City> filteredList = (ArrayList<City>)results.values;
			ArrayList<City> cityList = new ArrayList<>();
			if (results != null && results.count > 0) {
				clear();
				for (City c : filteredList) {
					cityList.add(c);
				}
				Iterator<City> customerIterator = cityList.iterator();
				while (customerIterator.hasNext()) {
					City cityToAdd = customerIterator.next();
					add(cityToAdd);
				}
				notifyDataSetChanged();
			}
		}
	};

}
