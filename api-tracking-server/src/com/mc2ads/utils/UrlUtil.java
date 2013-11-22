package com.mc2ads.utils;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {

	public static final String buildUrl(HttpServletRequest request, String uri){
		try {
			URL url = new URL(request.getRequestURL().toString());
			StringBuilder sb = new StringBuilder();
			sb.append(url.getProtocol()).append("://").append(url.getHost()).append(":").append(url.getPort());
			sb.append(uri);
			return sb.toString();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return uri;
	}
}
