package com.mc2ads.server.services;

import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;
import com.mc2ads.utils.ParamUtil;



@BaseRestHandler (uri = "hello")
public class HelloWorldHandler extends BaseServiceHandler {

	@Override
	@MethodRestHandler
	public String getServiceName() {		
		return this.getClass().getName();
	}
	
	@MethodRestHandler
	public String sayHi() {	
		//to call: request http://localhost:10001/hello/sayHi?name=Trieu Nguyen
		return "Hello from api rest server, " + ParamUtil.getString(request,"name");
	}

}
