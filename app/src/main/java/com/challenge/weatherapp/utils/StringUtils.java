package com.challenge.weatherapp.utils;

public class StringUtils {

	public static String commaSeparatedString(String[] string) {
		if (string.length > 0) {
			StringBuilder nameBuilder = new StringBuilder();
			for (String n : string) {
				nameBuilder.append(n).append(",");
			}
			nameBuilder.deleteCharAt(nameBuilder.length() - 1);
			return nameBuilder.toString();
		} else {
			return "";
		}
	}
}
