package com.mc2ads.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
	
	public static long toMinutes(long miliseconds){
		return miliseconds / 60000;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		System.out.println(now);
		long diff = (now - 1347439846515L);
		diff = diff / 60000;
		System.out.println("diff: " + diff);
	}

}
