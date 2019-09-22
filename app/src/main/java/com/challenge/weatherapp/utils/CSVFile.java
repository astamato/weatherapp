package com.challenge.weatherapp.utils;


import com.challenge.weatherapp.domain.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class CSVFile {

	private InputStream inputStream;

	CSVFile(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	List read() {
		List<City> resultList = new ArrayList();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String csvLine;
			while ((csvLine = reader.readLine()) != null) {
				String[] row = csvLine.split(",");
				City c = new City();
				c.setId(row[0]);
				c.setName(row[1]);
				c.setCountryCode(row[2]);
				resultList.add(c);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: " + ex);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException("Error while closing input stream: " + e);
			}
		}
		return resultList;
	}

}
