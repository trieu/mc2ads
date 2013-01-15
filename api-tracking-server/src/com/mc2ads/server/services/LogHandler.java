package com.mc2ads.server.services;

import java.util.Map;

import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;
import com.mc2ads.utils.HashUtil;
import com.mc2ads.utils.Log;
import com.mc2ads.utils.ParamUtil;

@BaseRestHandler(uri = "log")
public class LogHandler extends BaseServiceHandler {

	@Override
	@MethodRestHandler
	public String getServiceName(Map params) {
		return this.getClass().getName();
	}
	
	@MethodRestHandler
	public String track(Map params)  {
		
		String url = ParamUtil.getString(request, "url");
		String title = ParamUtil.getString(request, "title");
		String keywords = ParamUtil.getString(request, "keywords");
		String categories = ParamUtil.getString(request, "categories");
;;
		System.out.println(HashUtil.hashUrl(url));
		
		String fosp_aid = ParamUtil.getString(request, "fosp_aid");
		
		
		//TODO submit raw data to storm for real-time computation
		
		System.out.println("-------------------------------");				
		Log.println(url,title,keywords,fosp_aid,categories);
		System.out.println("-------------------------------");
		return "1";
	}

}
