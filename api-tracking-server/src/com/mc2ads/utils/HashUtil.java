package com.mc2ads.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author trieu
 * 
 * Hash function util
 *
 */
public class HashUtil {
	
	/**
	 * hash url to long number <br>
	 * 
	 * MurmurHash3 is the successor to MurmurHash2. <br>
	 * It comes in 3 variants - a 32-bit version that targets low latency for hash table use 
	 * and two 128-bit versions for generating unique identifiers 
	 * for large blocks of data, 
	 * one each for x86 and x64 platforms.
	 * 
	 * @param url
	 * @return long number
	 */
	public static long hashUrl(String url) {
		HashFunction hf = Hashing.murmur3_128();
		HashCode hc = hf.newHasher().putString(url).hash();
		return hc.asLong();
	}
}
