package com.mc2ads.server.autotasks;

import java.io.IOException;
import java.util.Collection;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mc2ads.utils.FileUtils;

public class AutoTaskConfigs {
	
	private String classpath;
	private long delay;
	private long period;
	
	public AutoTaskConfigs() {
		
	}
	
	
	public  String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("classpath:").append(classpath).append("-");
		s.append("delay:").append(delay).append("-");
		s.append("period:").append(period);		
		return s.toString();
	}


	public static Collection<AutoTaskConfigs> loadFromFile(String filePath){		
		try {
			String json = FileUtils.readFileAsString(filePath);			
			java.lang.reflect.Type collectionType = new TypeToken<Collection<AutoTaskConfigs>>(){}.getType();
			Collection<AutoTaskConfigs> ints2 = new Gson().fromJson(json, collectionType);						
			return ints2;
		} catch (IOException e) {
			e.printStackTrace();		
		}
		return null;
	}
}
