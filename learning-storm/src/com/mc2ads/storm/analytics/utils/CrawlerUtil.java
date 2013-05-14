package com.mc2ads.storm.analytics.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mc2ads.storm.analytics.model.Webpage;

public class CrawlerUtil {

	public static final Webpage getWebpage(String url){
		String html = HttpClientUtil.executeGet(url);		
		Document doc = Jsoup.parse(html, HTTP.UTF_8);
		Elements keywords = doc.select("meta[name=keywords]");
		System.out.println(keywords.attr("content"));
		Elements title = doc.select("title");
		Elements description = doc.select("meta[name=description]");
		System.out.println(description.attr("content"));
		String[] toks = keywords.attr("content").split(",");
		Webpage webpage = new Webpage();
		webpage.setCanonicalURL(url);
		webpage.setTitle(title.text());
		webpage.setMetaDescription(description.attr("content"));
		webpage.setMetaKeywords(ListUtil.toList(toks));
		return webpage;
	}
	static final String GOOGLE_BASE_URL = "http://www.google.com.vn/search?sourceid=chrome&ie=UTF-8&q=";
	public static List<String> guessKeywordsFromEmail(String email){
		List<String> keywords = null;
		try {
			String search = URLEncoder.encode(email, StringPool.UTF8);		
			String html = HttpClientUtil.executeGet(GOOGLE_BASE_URL + URLEncoder.encode(search, StringPool.UTF8));
			//System.out.println(html);
			Document doc = Jsoup.parse(html, HTTP.UTF_8);
			Elements nodes = doc.select("#ires h3 a");
			//System.out.println(nodes.attr("href"));
			Webpage webpage = null;
			if(nodes.size()>0){
				webpage = CrawlerUtil.getWebpage(nodes.get(0).attr("href"));
				keywords = webpage.getMetaKeywords();
				
			} else {
				List<GoogleSearchUtil.Result> rs = GoogleSearchUtil.search(email);
				if(rs.size()>0){
					webpage = CrawlerUtil.getWebpage(rs.get(0).getUrl());
					keywords = webpage.getMetaKeywords();
				}
			}

			if(keywords.size() == 0){
				keywords.add(webpage.getTitle().replaceAll(email, "").trim());
			}
		} catch (Exception e) {			
			e.printStackTrace();
			keywords = new ArrayList<String>(0); 
		}		
		return keywords;
	}
	
	public static void main(String[] args) {
		System.out.println(guessKeywordsFromEmail("ptnuongr13.khtn@gmail.com"));
	}
}
