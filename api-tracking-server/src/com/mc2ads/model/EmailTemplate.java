package com.mc2ads.model;

public class EmailTemplate {
	String title = "";
	String senderAddress = "";
	String content = "";
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(title).append("\n");
		s.append(senderAddress).append("\n");
		s.append(content).append("\n");
		return s.toString();
	}
}
