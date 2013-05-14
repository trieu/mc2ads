package com.mc2ads.storm.analytics.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mc2ads.storm.analytics.model.Webpage;


public class HttpClientUtil {	
	
	static HttpClient defaultHttpClient; 
	
	final static int DEFAULT_TIMEOUT = 30000;//30 seconds
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:9.0) Gecko/20100101 Firefox/9.0";
	public static final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.2; en-us; DROID2 GLOBAL Build/S273) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	
	public static final HttpClient getThreadSafeClient() throws Exception {
		if(defaultHttpClient == null){
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(
			         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			schemeRegistry.register(
			         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));	
			
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager( schemeRegistry);		 
			HttpClient httpClient = new DefaultHttpClient();
		    HttpParams params = httpClient.getParams();
		    HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
		    defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(mgr.getSchemeRegistry()), params);
		}
	    return defaultHttpClient;
	}
	
	public static boolean isValidHtml(String html){
		if(html == null){
			return false;
		}
		if (html.equals("404") || html.isEmpty() || html.equals("500")){
			return false;
		}
		return true;
	}
	
	public static final HttpClient makeThreadSafeClient() {
		HttpClient defaultHttpClient; 
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http",80, PlainSocketFactory.getSocketFactory()));
		
		ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(  schemeRegistry);		 
		HttpClient httpClient = new DefaultHttpClient();
	    HttpParams params = httpClient.getParams();
	    HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
	    defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(mgr.getSchemeRegistry()), params);
	
	    return defaultHttpClient;
	}
	
	public static final HttpClient theThreadSafeHttpClient() {
		HttpParams params = new BasicHttpParams();
	    SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
	    ClientConnectionManager cm = new ThreadSafeClientConnManager(registry);
	    HttpClient client = new DefaultHttpClient(cm, params);
	    return client;
	}
	
	public static String executePost(String url,Map<String, String> params, String accessTokens) {
		try {
			HttpClient httpClient = getThreadSafeClient();
			HttpPost postRequest = new HttpPost(url);			
			postRequest.addHeader("Accept-Charset", HTTP.UTF_8);
			postRequest.addHeader("User-Agent", MOBILE_USER_AGENT);
			postRequest.setHeader("Authorization", "OAuth oauth_token="+accessTokens);

			Set<String> names = params.keySet();
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>(names.size());					
			for (String name : names) {
				System.out.println( name + "=" + params.get(name));
				postParameters.add(new BasicNameValuePair(name, params.get(name)));
			}			
			postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
			
			HttpResponse response = httpClient.execute(postRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				httpClient.getConnectionManager().closeExpiredConnections();
				return EntityUtils.toString(entity, HTTP.UTF_8);
			}
		} catch (HttpResponseException e) {
			System.err.println(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String executeMultipartPost(String postUrl,Map<String, Object> params, String accessTokens)
			throws Exception {
		try {
			HttpClient httpClient = getThreadSafeClient();
			HttpPost postRequest = new HttpPost(postUrl);			
			postRequest.addHeader("Accept-Charset", HTTP.UTF_8);
			postRequest.addHeader("User-Agent", MOBILE_USER_AGENT);			
			postRequest.setHeader("Cache-Control", "max-age=3, must-revalidate, private");	
			postRequest.setHeader("Authorization", "OAuth oauth_token="+accessTokens+", oauth_consumer_key=a324957217164fd1d76b4b60d037abec, oauth_version=1.0, oauth_signature_method=HMAC-SHA1, oauth_timestamp=1322049404, oauth_nonce=-5195915877644743836, oauth_signature=wggOr1ia7juVbG%2FZ2ydImmiC%2Ft4%3D");

			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			Set<String> names = params.keySet();
			for (String name : names) {
				Object value = params.get(name);
				if (value instanceof StringBody) {
					reqEntity.addPart(name, (StringBody) value);
				} else if (value instanceof File) {
					reqEntity.addPart("media", new FileBody((File) value));
				}
			}
			postRequest.setEntity(reqEntity);

			HttpResponse response = httpClient.execute(postRequest);
			return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		} catch (Exception e) {
			// handle exception here
			e.printStackTrace();
		}
		return "";
	}
	
	public static String executePost(String url){
		try {	
			HttpPost httppost = new HttpPost(url);
			
			httppost.setHeader("User-Agent", USER_AGENT);
			httppost.setHeader("Accept-Charset", "utf-8");			
			httppost.setHeader("Cache-Control", "max-age=3, must-revalidate, private");	
			httppost.setHeader("Authorization", "OAuth oauth_token=2d62f7b3de642cdd402f62e42fba0b25, oauth_consumer_key=a324957217164fd1d76b4b60d037abec, oauth_version=1.0, oauth_signature_method=HMAC-SHA1, oauth_timestamp=1322049404, oauth_nonce=-5195915877644743836, oauth_signature=wggOr1ia7juVbG%2FZ2ydImmiC%2Ft4%3D");

			HttpResponse response = getThreadSafeClient().execute(httppost);
			HttpEntity entity = response.getEntity();				
			if (entity != null) {
				getThreadSafeClient().getConnectionManager().closeExpiredConnections();
				return EntityUtils.toString(entity, HTTP.UTF_8);
			}
		
		}  catch (HttpResponseException e) {
		    System.err.println(e.getMessage());		  
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return "";
	}
	
	public static String executeGet(final URL url, boolean safeThread){
		HttpResponse response = null;
		HttpClient httpClient = null;
		//System.out.println("executeGet:" + url);
		try {
			HttpGet httpget = new HttpGet(url.toURI());			
			httpget.setHeader("User-Agent", USER_AGENT);
			httpget.setHeader("Accept-Charset", "utf-8");
			httpget.setHeader("Accept", "text/html,application/xhtml+xml");
			httpget.setHeader("Cache-Control", "max-age=3, must-revalidate, private");	
			//httpget.addHeader(BasicScheme.authenticate(	 new UsernamePasswordCredentials("ejgsadmin", "6uCdS7cA3"),"UTF-8", false));
			//httpget.setHeader("Authorization", "OAuth oauth_token=223a363ea1fd0a13b44e52663b97a255, oauth_consumer_key=a324957217164fd1d76b4b60d037abec, oauth_version=1.0, oauth_signature_method=HMAC-SHA1, oauth_timestamp=1322049404, oauth_nonce=-5195915877644743836, oauth_signature=wggOr1ia7juVbG%2FZ2ydImmiC%2Ft4%3D");
			
			if(safeThread){
				httpClient = getThreadSafeClient();
				response = httpClient.execute(httpget);
			} else { 
				httpClient = makeThreadSafeClient();
				response = httpClient.execute(httpget);
			}
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {				
				HttpEntity entity = response.getEntity();
				if (entity != null) {										
					String html = EntityUtils.toString(entity, HTTP.UTF_8);
					return html;
				}
			} else if(code == 404) {
				return "404";
			} else {
				return "500";
			}
		}  catch (Throwable e) {
			if( e instanceof java.net.ConnectException){
				return "444";
			} 
			e.printStackTrace(); 
		} finally {
			if(httpClient != null) {
				httpClient.getConnectionManager().closeExpiredConnections();
				httpClient = null;
			}
			response = null;
		}
		return "";
	}
	
	public static String executeGet(final String url, boolean safeThread){
		try {
			return executeGet(new URL(url), safeThread);
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		}
		return "";
	}
	
	public static String executeGet(final String url, boolean safeThread, boolean redownload500, int numRetry){		
		try {
			if(redownload500){
				String html = executeGet(new URL(url), safeThread);
				while( html.equals("500") ){
					Thread.sleep(400);
					html = executeGet(new URL(url), safeThread);					
					numRetry --;
					if(numRetry <= 0){
						break;
					}
				}				
				return html;
			} else {
				return executeGet(new URL(url), safeThread);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return "";
	}	
	
	
	public static String executeGet(String baseUrl, Map<String, Object> params) {
		if( ! baseUrl.contains("?")){
			baseUrl += "?";
		}		
		StringBuilder url = new StringBuilder(baseUrl);
		Set<String> ps = params.keySet();
		int c=1, s=params.size()-1;
		try {
			for (String p : ps) {
				String v = URLEncoder.encode(params.get(p).toString().trim(), StringPool.UTF8);
				if(!v.equals(StringPool.BLANK)){
					p = URLEncoder.encode(p, StringPool.UTF8);					
					url.append(p).append("=").append(v);
					if(c<s){
						url.append("&");
					}
					c++;
				}
			}
		} catch (UnsupportedEncodingException e) {}
		return executeGet(url.toString(), true);
	}
	
	
	public static String executeGet(final String url){		
		return executeGet(url, true);
	}
	
	public static void main(String[] args){
		String html = HttpClientUtil.executeGet("http://www.google.com.vn/search?q=jrexroadjr@yahoo.com&sourceid=chrome&ie=UTF-8");
		//System.out.println(html);
		Document doc = Jsoup.parse(html, HTTP.UTF_8);
		Elements nodes = doc.select("#ires h3 a");
		System.out.println(nodes.attr("href"));
		Webpage webpage = CrawlerUtil.getWebpage(nodes.attr("href"));
		System.out.println(webpage);
	}
	
	
	
}
