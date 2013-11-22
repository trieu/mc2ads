package com.mc2ads.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	
	static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	public static String formatDate(Date d ){
		return DATE_FORMAT.format(d);
	}
	
	public static Date parseDateStr(String str){
		try {
			return DATE_FORMAT.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
}
