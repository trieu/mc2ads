package com.mc2ads.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mc2ads.utils.FileUtils;

public class CommonConfigs {
	private List<String> baseParkageForHandlers;
	private String dataNodePath;
	private int port = 10001;
	
	private static CommonConfigs _instance;
	
	public static final CommonConfigs load(){
		if(_instance == null){
			try {
				String json = FileUtils.readFileAsString("config.json");
				_instance =  new Gson().fromJson(json, CommonConfigs.class);
			} catch (IOException e) {
				e.printStackTrace();		
			}
		}
		return _instance;
	}

	public CommonConfigs(String dataNodePath) {
		super();
		this.dataNodePath = dataNodePath;
	}

	public CommonConfigs() {
		super();
	}

	public String getDataNodePath() {
		return dataNodePath;
	}

	public void setDataNodePath(String dataNodePath) {
		this.dataNodePath = dataNodePath;
	}
	
	public List<String> getBaseParkageForHandlers() {
		if(baseParkageForHandlers == null){
			baseParkageForHandlers = new ArrayList<String>();
		}
		return baseParkageForHandlers;
	}

	public void setBaseParkageForHandlers(List<String> baseParkageForHandlers) {
		this.baseParkageForHandlers = baseParkageForHandlers;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("this.dataNodePath " + this.dataNodePath);
		return sb.toString();
	}
	
}
