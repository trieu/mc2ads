package com.mc2ads.storm.analytics.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static List<String> toList(String[] toks){
		List<String> list = new ArrayList<String>(toks.length);
		for (String tok : toks) {
			if( ! StringPool.BLANK.equals(tok) )				
				list.add(tok);
		}
		return list;
	}
}
