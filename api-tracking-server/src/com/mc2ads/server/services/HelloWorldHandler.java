package com.mc2ads.server.services;

import java.util.Map;

import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;



@BaseRestHandler (uri = "hello")
public class HelloWorldHandler extends BaseServiceHandler {

	@Override
	@MethodRestHandler
	public String getServiceName(Map params) {		
		return this.getClass().getName();
	}
	
	@MethodRestHandler
	public String sayHi(Map params) {	
		//to call: request http://localhost:10001/hello/sayHi?name=Trieu Nguyen
		return "Hello, " + params.get("name");
	}

}
