package com.mc2ads.server;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mc2ads.server.annotations.MethodRestHandler;



public abstract class BaseServiceHandler {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}
	
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.response = httpServletResponse;
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return request;
	}
	
	public HttpServletResponse getHttpServletResponse() {
		return response;
	}
	
	@MethodRestHandler
	public abstract String getServiceName(Map params );

}
