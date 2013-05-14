package com.mc2ads.storm.analytics.utils;

import org.jboss.netty.buffer.ChannelBuffer;

public class NettyUtils {

	public static String readString(ChannelBuffer buf) {
		StringBuilder sb = new StringBuilder();
		while (buf.readable()) {
			sb.append((char) buf.readByte());
		}

		String message = sb.toString();
		return message;
	}

}
