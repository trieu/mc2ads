package com.mc2ads.storm.analytics.utils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class GoogleSearchUtil {

	public static class ResponseData {
		private List<Result> results;

		public List<Result> getResults() {
			return results;
		}

		public void setResults(List<Result> results) {
			this.results = results;
		}

		public String toString() {
			return "Results[" + results + "]";
		}
	}

	public static class Result {
		private String url;
		private String title;

		public String getUrl() {
			return url;
		}

		public String getTitle() {
			return title;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String toString() {
			return "Result[url:" + url + ",title:" + title + "]";
		}
	}

	public static class GoogleResults {

		private ResponseData responseData;

		public ResponseData getResponseData() {
			return responseData;
		}

		public void setResponseData(ResponseData responseData) {
			this.responseData = responseData;
		}

		public String toString() {
			return "ResponseData[" + responseData + "]";
		}
	}
	
	static final String GOOGLE_BASE_URL = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&safe=active&rsz=8&q=";
	public static List<Result> search(String keyword){
		try {
			URL url = new URL(GOOGLE_BASE_URL + URLEncoder.encode(keyword, StringPool.UTF8));
			Reader reader = new InputStreamReader(url.openStream(), StringPool.UTF8);
			GoogleResults results = new Gson()
					.fromJson(reader, GoogleResults.class);

			// Show title and URL of 1st result.
			if(results.getResponseData() != null){
				List<Result> list = results.getResponseData().getResults();
				return list;
			}
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return new ArrayList<Result>(0);
	}

	public static void main(String[] args) throws Exception {
		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&safe=active&rsz=8&q=";
		String search = "đường hoa Nguyễn Huệ site:vnexpress.net";
		String charset = "UTF-8";

		URL url = new URL(google + URLEncoder.encode(search, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson()
				.fromJson(reader, GoogleResults.class);

		// Show title and URL of 1st result.
		List<Result> list = results.getResponseData().getResults();
		for (Result result : list) {
			System.out.println(result.getTitle());
			System.out.println(result.getUrl()+"\n");
		}		
	}
}
