package com.mc2ads.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.mc2ads.utils.FileUtils;

public class SqlDbConfigs {
	
	public final static String DB_CONFIG_FILE_NAME = "dbconfigs.json"; 
	public final static String MY_SQL = "mysql";
	public final static String SQL_SERVER = "sqlserver";
	public final static String ORACLE = "oracle";
	
	public static class SqlDbConfigsMap {
		private HashMap<String, SqlDbConfigs> map;
		public SqlDbConfigsMap() {}
		public HashMap<String, SqlDbConfigs> getMap() {
			if(map == null){
				map = new HashMap<String, SqlDbConfigs>(0);
			}
			return map;
		}
		public void setMap(HashMap<String, SqlDbConfigs> map) {
			this.map = map;
		}		
	}
	
	private String username;
	private String password;
	private String database;
	private String host;
	private String dbdriver;
	private String dbdriverclasspath;
	
	
	static final Map<String,SqlDbConfigs> sqlDbConfigsCache = new HashMap<String,SqlDbConfigs>(5);
	static final Map<String,Driver> driversCache = new HashMap<String, Driver>();
	static String sqlDbConfigsJson = null;
	
	public static SqlDbConfigs load(String siteId){		
		return loadFromFile(DB_CONFIG_FILE_NAME,siteId);
	}
	
	public static SqlDbConfigs loadFromFile(String filePath, String siteId){		
		String k = filePath + siteId;
		if(sqlDbConfigsJson == null){
			try {
				sqlDbConfigsJson = FileUtils.readFileAsString(filePath);
			} catch (IOException e) {
				throw new IllegalArgumentException("File is not found at " + filePath);
			}
		}
		if( ! sqlDbConfigsCache.containsKey(k) ){
			SqlDbConfigs configs = null;
			try {						
				SqlDbConfigsMap map =  new Gson().fromJson(sqlDbConfigsJson, SqlDbConfigsMap.class);			
				configs = map.getMap().get(siteId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(configs == null){
				throw new IllegalArgumentException("CAN NOT LOAD SqlDbConfigs from " + filePath);
			}
			sqlDbConfigsCache.put(k, configs);
		}		
		return sqlDbConfigsCache.get(k);
	}
	
	
	public String getConnectionUrl(){
		StringBuilder s = new StringBuilder();		
		if(MY_SQL.equals(this.getDbdriver())){
			s.append("jdbc:").append(MY_SQL).append("://");
			s.append(this.getHost());
			s.append(":3306/");
			s.append(this.getDatabase());
			s.append("?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8");			
		} else if(SQL_SERVER.equals(this.getDbdriver())){
			s.append("jdbc:").append(SQL_SERVER).append("://");
			s.append(this.getHost());
			s.append(";databaseName=");
			s.append(this.getDatabase());
		} else if(ORACLE.equals(this.getDbdriver())){
			//"jdbc:oracle:thin:@10.254.53.220:1521:ORADEV1"
			s.append("jdbc:").append(ORACLE).append(":thin:@");
			s.append(this.getHost());
			s.append(":1521:");
			s.append(this.getDatabase());
		} else {
			throw new IllegalArgumentException("Currently, only support JDBC driver for MySQL, MSSQL Server,Oracle!");
		}
		return s.toString();
	}
	
	public Connection getConnection() throws SQLException{
		Connection dbConnection = null;
		try {			
			Driver driver = driversCache.get(getDbdriverclasspath());
			String connectionUrl = getConnectionUrl();
			if(driver == null){
				driver = (Driver) Class.forName(getDbdriverclasspath()).newInstance();
				driversCache.put(getDbdriverclasspath(), driver);
				DriverManager.registerDriver(driver);
			}			
			dbConnection = DriverManager.getConnection(connectionUrl, getUsername(), getPassword());
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Missing JDBC driver jar for " + this.getDbdriverclasspath());
		} catch (InstantiationException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {		
			e.printStackTrace();
		} 
		return dbConnection;
	}
	
	
	
	public SqlDbConfigs() {
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getDbdriver() {
		return dbdriver;
	}
	public void setDbdriver(String dbdriver) {
		this.dbdriver = dbdriver;
	}
	public String getDbdriverclasspath() {
		return dbdriverclasspath;
	}
	public void setDbdriverclasspath(String dbdriverclasspath) {
		this.dbdriverclasspath = dbdriverclasspath;
	}
}
