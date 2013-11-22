package com.mc2ads.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.mc2ads.db.SqlDbConfigs;

public class DataManager {

	private static Map<String, DataSource> _dataSources = new HashMap<String, DataSource>();

	public static DataSource getNewDataSource(String db) {		
		DataSource _dataSource = setupDataSource(SqlDbConfigs.load(db));			
		return _dataSource;
	}
	public static DataSource getDataSource(String db) {		
		DataSource _dataSource = _dataSources.get(db);
		if(_dataSource  == null){
			_dataSource = setupDataSource(SqlDbConfigs.load( db));
			_dataSources.put(db, _dataSource);
		}
		return _dataSource;
	}

	public static DataSource setupDataSource(SqlDbConfigs configs) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(configs.getDbdriverclasspath());
		ds.setUsername(configs.getUsername());
		ds.setPassword(configs.getPassword());
		ds.setUrl(configs.getConnectionUrl());
		return ds;
	}

	public static void printDataSourceStats(DataSource ds) {
		BasicDataSource bds = (BasicDataSource) ds;
		System.out.println("NumActive: " + bds.getNumActive());
		System.out.println("NumIdle: " + bds.getNumIdle());
	}

	public static void shutdownDataSource(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}

	public DataManager() {
		super();
	}

}