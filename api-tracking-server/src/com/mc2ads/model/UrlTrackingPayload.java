package com.mc2ads.model;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.mc2ads.utils.ParamUtil;

public class UrlTrackingPayload {
	String url ;
	String title;
	String keywords;
	String categories;
	String uuid;
	
	public UrlTrackingPayload(  HttpServletRequest request ){
		url = ParamUtil.getString(request, "url", "");
		title = ParamUtil.getString(request, "title", "");
		keywords = ParamUtil.getString(request, "keywords", "");
		categories = ParamUtil.getString(request, "categories", "");
		uuid = ParamUtil.getString(request, "uuid","");
	}
	
	public UrlTrackingPayload(String url, String title, String keywords,
			String categories) {
		super();
		this.url = url;
		this.title = title;
		this.keywords = keywords;
		this.categories = categories;
	}
	public String getUrl() {
		return url;
	}
	public String getTitle() {
		return title;
	}
	public String getKeywords() {
		return keywords;
	}
	public String getCategories() {
		return categories;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String toJson(){
		return new Gson().toJson(this);
	}
	
}
