package com.mc2ads.utils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Template;

public class TemplateUtils {
	
	static final UrlUtil URL_UTIL = new UrlUtil();  
	
	static Logger logger = Logger.getLogger(TemplateUtils.class);
	
	public static String processModel(Map<String, Object> dataModel, String relativePath, String templateName )   {
		StringWriter writer = new StringWriter();
		try {
			dataModel.put("", "");
			dataModel.put("UrlUtil", URL_UTIL);
			Template template = FreemarkerTemplateEngine.getTemplate(relativePath, templateName);			
			template.process(dataModel, writer);
		} catch (Exception e) {
			logger.error(e);
			//e.printStackTrace();
		}
		writer.flush();		
		return writer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("search_results", "aaa");
		String str = processModel(dataModel, "search/", "search-view.ftl");
		System.out.println(str);

	}

}
