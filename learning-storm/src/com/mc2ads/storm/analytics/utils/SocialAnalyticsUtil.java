package com.mc2ads.storm.analytics.utils;


import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;




public class SocialAnalyticsUtil {

	public static JSONArray getLinkFacebookStats(String url){
		String baseUrl = "http://api.facebook.com/method/fql.query?query=SELECT+url%2C+normalized_url%2C+share_count%2C+like_count%2C+comment_count%2C+total_count%2C+commentsbox_count%2C+comments_fbid%2C+click_count+FROM+link_stat+WHERE+url%3D%22"+url+"%22&format=json";
		JSONArray obj = null;
		try {
			String json = HttpClientUtil.executeGet(baseUrl);
			//System.out.println(json);
			obj = new twitter4j.internal.org.json.JSONArray(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Elements getLinks(String url){
		try {
			String html = HttpClientUtil.executeGet(url);
			Document doc = Jsoup.parse(html);			
			return doc.select("a[href]");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Elements(0);		
	}
	
	public static int getVnExpressLikeCount(String url){
		try {
			String html = HttpClientUtil.executeGet(url);
			Document doc = Jsoup.parse(html);			
			Elements spans = doc.select(".likecount");
			if(spans.size()>0){
				String text = spans.get(0).text();
				return Integer.parseInt(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;		
	}
	
	public static int getTotalCountFacebookStats(String url){
		JSONArray array = getLinkFacebookStats(url);
		System.out.println(array);
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				return obj.getInt("total_count");
			}
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public static JSONObject getLinkInfoOnFacebook(String url){
		String baseUrl = "http://graph.facebook.com/?ids=";
		JSONObject obj = null;
		try {
			baseUrl = baseUrl +  URLEncoder.encode(url, StringPool.UTF8);
			obj = new twitter4j.internal.org.json.JSONObject(HttpClientUtil.executeGet(baseUrl));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static int getTweetCount(String url){
		String baseUrl = "http://urls.api.twitter.com/1/urls/count.json?url=";
		JSONObject obj = null;
		try {
			baseUrl = baseUrl +  URLEncoder.encode(url, StringPool.UTF8);
			obj = new twitter4j.internal.org.json.JSONObject(HttpClientUtil.executeGet(baseUrl));
			return obj.getInt("count");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getTotalUserLikeUrl(String url){
		int c = getTotalCountFacebookStats(url);
		c += getTweetCount(url);
		c += getVnExpressLikeCount(url);
		return c;
	}
	
	public static  void main(String[] args){
		String url = "http://giaitri.vnexpress.net/tin-tuc/nhac/lang-nhac/nhac-si-pham-duy-qua-doi-2419616.html";
		getTotalCountFacebookStats("http://vnexpress.net/gl/xa-hoi/2013/01/duong-hoa-nguyen-hue-van-hoa-tet-cua-sai-gon/");
	}
	
	public static void main2(String[] args) {
		String domain = "vnexpress.net";
		String testurl = "http://vnexpress.net/gl/kinh-doanh/quoc-te/2012/12/10-quang-cao-hay-nhat-nam-2012/";
		System.out.println(getTweetCount(testurl));
		//System.out.println(getLinkInfoOnFacebook(testurl));
		System.out.println(getTotalCountFacebookStats(testurl));
		System.out.println(getVnExpressLikeCount(testurl));
		System.out.println(getTotalUserLikeUrl(testurl));
		Elements nodes = getLinks(testurl);
		for (Element element : nodes) {
			String href = element.attr("href");
			if(href != null){
				href = href.trim();
				boolean ok = false;
				if( ! href.startsWith("javascript")  && ! href.startsWith("#") && href.length()>1 ){
					if(href.startsWith("http")&&href.contains(domain)){
						ok = true;
					} else if(href.startsWith("/")){
						ok = true;
						href = "http://" + domain + href;
					}
					if(ok){
						System.out.println(href);	
					}					
				}
				
			}
		}
		
	}
}
