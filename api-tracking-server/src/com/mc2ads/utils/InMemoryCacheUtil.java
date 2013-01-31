package com.mc2ads.utils;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class InMemoryCacheUtil {
	public static void main(String[] args) {
		CacheLoader<String, String> loader = new CacheLoader<String, String>() {
			public String load(String key) {
				return "";
			}
		};

		LoadingCache<String, String> cache = CacheBuilder.newBuilder()
				.maximumSize(200)
				.expireAfterWrite(5, TimeUnit.SECONDS)
				.build(loader);
		cache.put("a1", "ss1");
		cache.put("a2", "ss2");
		try {
			System.out.println(cache.getIfPresent("a1"));
			System.out.println(cache.getUnchecked("a2"));
			Thread.sleep(6000);
			System.out.println(cache.getUnchecked("a1"));
			System.out.println(cache.getIfPresent("a2"));
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
}
