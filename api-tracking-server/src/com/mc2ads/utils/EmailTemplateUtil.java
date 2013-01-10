package com.mc2ads.utils;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mc2ads.model.EmailTemplate;

public class EmailTemplateUtil {
	public static EmailTemplate parse(String emailTplName){
		EmailTemplate template = new EmailTemplate();
		Map<String, Object> dataModel = new HashMap<String, Object>();

		String html = TemplateUtils.processModel(dataModel, "email/", emailTplName + ".ftl");
		Document doc = Jsoup.parse(html);
		Elements metas = doc.select("meta");
		for (Element meta : metas) {
			String name = meta.attr("name");
			String content = meta.attr("content");
			if(name.equals("email_sender")){
				template.setSenderAddress(content);
			} else if(name.equals("email_title")){
				template.setTitle(content);
			}
		}
		Elements body = doc.select("body");
		template.setContent(body.html());
		return template;
	}
}
