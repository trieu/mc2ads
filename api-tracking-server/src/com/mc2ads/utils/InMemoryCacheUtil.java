package com.mc2ads.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class InMemoryCacheUtil {
	static LoadingCache<String, String> cache;
	public static void main(String[] args) {
		
		CacheLoader<String, String> loader = new CacheLoader<String, String>() {
			public String load(String key) {
				if(key.equals("a1")){					
					String v = "ss1-"+System.currentTimeMillis();
					cache.put(key, v);
					try {
						return cache.get(key);
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return v;
				}
				return "";
			}
		};

		cache = CacheBuilder.newBuilder()
				.maximumSize(200)
				.expireAfterWrite(5, TimeUnit.SECONDS)
				.build(loader);
		cache.put("a1", "ss1");
		cache.put("a2", "ss2");
		try {
			System.out.println(cache.getIfPresent("a1"));
			System.out.println(cache.getUnchecked("a2"));
			System.out.println(cache.get("a1"));
			Thread.sleep(6000);
			System.out.println(cache.getUnchecked("a1"));
			System.out.println(cache.getIfPresent("a2"));
			System.out.println(cache.get("a1"));
			Thread.sleep(2000);
			System.out.println(cache.get("a1"));
			Thread.sleep(3001);
			System.out.println(cache.get("a1"));
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
}
