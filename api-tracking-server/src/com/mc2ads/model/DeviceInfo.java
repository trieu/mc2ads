package com.mc2ads.model;

import java.io.Serializable;

public class DeviceInfo implements Serializable{

	private static final long serialVersionUID = -5928631048327859155L;
	long id;
	int user_id;
	String device_token;
	String device_model;
	String os_name;
	String os_version;
	int notify_count;
	long creation_time;
	long last_notify_time;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public String getOs_name() {
		return os_name;
	}
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
	public String getOs_version() {
		return os_version;
	}
	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}
	public int getNotify_count() {
		return notify_count;
	}
	public void setNotify_count(int notify_count) {
		this.notify_count = notify_count;
	}
	public long getCreation_time() {
		return creation_time;
	}
	public void setCreation_time(long creation_time) {
		this.creation_time = creation_time;
	}
	public long getLast_notify_time() {
		return last_notify_time;
	}
	public void setLast_notify_time(long last_notify_time) {
		this.last_notify_time = last_notify_time;
	}
	
}
