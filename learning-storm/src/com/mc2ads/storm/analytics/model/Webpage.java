package com.mc2ads.storm.analytics.model;

import java.util.ArrayList;
import java.util.List;

public class Webpage {
	
	long id;
	String title;
	List<String> metaKeywords;
	String metaDescription;
	String canonicalURL;
	String content;			
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		if(title == null){
			title = "";
		}
		return title;
	}
	public void setTitle(String title) {
		if(title != null){
			title = title.replaceAll("\\n", "");
			this.title = title;
		}
	}
	public List<String> getMetaKeywords() {
		if(metaKeywords == null){
			metaKeywords = new ArrayList<String>(0);
		}
		return metaKeywords;
	}
	public void setMetaKeywords(List<String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	public String getContent() {
		if(content == null){
			content = "";
		}
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getCanonicalURL() {
		return canonicalURL;
	}
	public void setCanonicalURL(String canonicalURL) {
		this.canonicalURL = canonicalURL;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(getTitle()).append(" # ");
		s.append(getMetaDescription()).append(" # ");
		s.append(getMetaKeywords()).append(" # ");
		s.append(getCanonicalURL()).append(" # ");
		return s.toString();
	}
}
