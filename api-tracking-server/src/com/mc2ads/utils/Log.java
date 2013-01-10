package com.mc2ads.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {

	public static Logger get(Class clazz) {
		Logger logger = Logger.getLogger(clazz.getName());
		logger.setLevel(Level.INFO);

		return logger;
	}

	public static final int NO_LOG = 0;
	public static final int PRINT_CONSOLE = 1;

	public static volatile int MODE = PRINT_CONSOLE;

	public static void println(String s) {
		if (MODE == PRINT_CONSOLE) {
			System.out.println(s);
		}
	}

	public static void println(Object s) {
		if (MODE == PRINT_CONSOLE) {
			System.out.println(s);
		}
	}
	
	public static void println(Object ... args) {
		if (MODE == PRINT_CONSOLE) {
			for (Object object : args) {
				println(object);
				
			}
		}
	}

	public static void setPropertyConfiguratorLog4J(String classname) {
		// Read properties file.
		Properties properties = new Properties();
		try {
			if(classname.isEmpty()){
				properties.load(new FileInputStream("log4j.properties"));				
			} else {
				properties.load(new FileInputStream("log4j-"+classname+".properties"));
				properties.put("log4j.appender.rollingFile.File", classname	+ ".log");
			}
		} catch (IOException e) {
		}
		PropertyConfigurator.configure(properties);
	}
	
	static Properties properties = null;
	
	public static void setLogPropertiesByDate(){
		if(properties != null){
			properties.clear();
			properties = null;
		}
		properties = new Properties();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");		    
			properties.load(new FileInputStream("log4j.properties"));
			properties.put("log4j.appender.rollingFile.File", "log/" + dateFormat.format(new Date()) + "-crawler-task.log");		
			PropertyConfigurator.configure(properties);
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger(Class clazz){	
		if(properties == null){
			setLogPropertiesByDate();
		}
		return Logger.getLogger(clazz);
	}
	
	public static void main(String[] args) {
		//Log.setPropertyConfiguratorLog4J("");
		Logger logger = getLogger(Log.class);
		logger.info("test");		
	}
}
