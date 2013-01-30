package com.mc2ads.server.services;

import java.util.HashMap;
import java.util.Map;

import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;
import com.mc2ads.utils.HashUtil;
import com.mc2ads.utils.Log;
import com.mc2ads.utils.ParamUtil;
import com.mc2ads.utils.TemplateUtils;

@BaseRestHandler(uri = "log")
public class LogHandler extends BaseServiceHandler {

	@Override
	@MethodRestHandler
	public String getServiceName() {
		return this.getClass().getName();
	}
	final static String RS = "1";
	
	@MethodRestHandler
	public String track()  {
		
		final String url = ParamUtil.getString(request, "url");
		final String title = ParamUtil.getString(request, "title","");
		final String keywords = ParamUtil.getString(request, "keywords","");
		final String categories = ParamUtil.getString(request, "categories","");

		System.out.println(HashUtil.hashUrl(url));
		
		final String fosp_aid = ParamUtil.getString(request, "fosp_aid");
		
		
		//TODO submit raw data to storm for real-time computation
		
		System.out.println("-------------------------------");				
		Log.println(url,title,keywords,fosp_aid,categories);
		System.out.println("-------------------------------");
		return RS;
	}
	
	@MethodRestHandler
	public String stats(){
		
		this.response.setContentType("text/html");

		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("request", this.request);
		dataModel.put("response", this.response);			
		String tpl_name = ParamUtil.getString(request, "tpl_name","");
		if(tpl_name.isEmpty()){
			return "";
		}
		return TemplateUtils.processModel(dataModel, "", tpl_name + ".ftl");
		
	}

}
