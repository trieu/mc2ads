package com.mc2ads.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.gson.Gson;

public class AndroidGcmUtil {
	final static String GCM_KEY = "";
	
	public static boolean notify(String deviceId){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://android.googleapis.com/gcm/send");
		
		List<String> registration_ids = new ArrayList<String>();
		registration_ids.add(deviceId);
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("cmd","refresh");
		
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("registration_ids", registration_ids);
		payload.put("data", data);
		boolean ok = false;
		try {

			post.addHeader("Authorization", "key=" + GCM_KEY);
			post.addHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(new Gson().toJson(payload)));

			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			JSONObject jsonObject = new JSONObject(result.toString());
			ok = jsonObject.getInt("success") == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok;
	}

	public static void main(String[] args) {
		String deviceId = "";
		System.out.println(notify(deviceId));
	}

}
