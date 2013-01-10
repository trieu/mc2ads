package com.mc2ads.db;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.mc2ads.utils.FileUtils;

public class SqlDbConfigs {
	public final static String DB_CONFIG_FILE_NAME = "dbconfigs.json"; 
	public final static String MY_SQL = "mysql";
	public final static String SQL_SERVER = "sqlserver";
	
	public static class SqlDbConfigsMap {
		private HashMap<Integer, SqlDbConfigs> map;
		public SqlDbConfigsMap() {}
		public HashMap<Integer, SqlDbConfigs> getMap() {
			if(map == null){
				map = new HashMap<Integer, SqlDbConfigs>(0);
			}
			return map;
		}
		public void setMap(HashMap<Integer, SqlDbConfigs> map) {
			this.map = map;
		}		
	}
	
	private String username;
	private String password;
	private String database;
	private String host;
	private String dbdriver;
	private String dbdriverclasspath;
	
	
	static Map<String,SqlDbConfigs> sqlDbConfigsCache = new HashMap<String,SqlDbConfigs>(5);
	
	public static SqlDbConfigs loadFromFile(String siteId){		
		return loadFromFile(DB_CONFIG_FILE_NAME,siteId);
	}
	
	public static SqlDbConfigs loadFromFile(String filePath, String siteId){		
		String k = filePath + siteId;
		if( ! sqlDbConfigsCache.containsKey(k) ){
			SqlDbConfigs configs = null;
			try {
				String json = FileUtils.readFileAsString(filePath);			
				SqlDbConfigsMap map =  new Gson().fromJson(json, SqlDbConfigsMap.class);			
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
			s.append("jdbc:").append(this.getDbdriver()).append("://");
			s.append(this.getHost());
			s.append("/");
			s.append(this.getDatabase());
			s.append("?autoReconnect=true");			
		} else if(SQL_SERVER.equals(this.getDbdriver())){
			s.append("jdbc:").append(this.getDbdriver()).append("://");
			s.append(this.getHost());
			s.append(";databaseName=");
			s.append(this.getDatabase());
		} else {
			throw new IllegalArgumentException("Currently, only support JDBC driver for MySQL, MSSQL Server!");
		}
		return s.toString();
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
