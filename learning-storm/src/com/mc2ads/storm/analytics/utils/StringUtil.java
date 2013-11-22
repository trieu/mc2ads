package com.mc2ads.storm.analytics.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.zip.CRC32;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

public class StringUtil {
	public static String CRC32(String s) {
		try {
			CRC32 crc32 = new CRC32();
			crc32.update(s.getBytes());
			byte[] bytesOfMessage = s.getBytes("UTF-8");

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			return new String(Hex.encodeHex(thedigest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String html2text(String html) {
		return Jsoup.parse(html).text();
	}

	public static boolean isStringContain(String text, String words) {
		// Pattern p = Pattern.compile("YOUR_REGEX", Pattern.CASE_INSENSITIVE |
		// Pattern.UNICODE_CASE);
		// p.m
		return text.matches("(?i)(.*)" + words + "(.*)");
	}

	private static String byteArray2Hex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

	public static String SHA1(byte[] convertme) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			return byteArray2Hex(md.digest(convertme));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String md5(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			// convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main1(String[] args) {

		String text = "Ngày 9/3, Phó chủ tịch Hà Nội Nguyễn Văn Khôi chỉ đạo Sở Giao thông Vận tải tăng thêm các tuyến phố cấm đỗ xe, trình thành phố trong tháng 4.> Người dân chật vật tìm chỗ gửi xe>'Hà Nội chưa quan tâm đầu tư bãi đỗ xe'";
		System.out.println(isStringContain(text, "ha Nội"));

		String hex = Integer.toHexString(2000000184);
		StringBuilder sb = new StringBuilder();
		int l = hex.length();
		for (int i = 0; i < l; i++) {
			sb.append(hex.charAt(i));
			if (i > 0 && ((i + 1) % 2 == 0)) {
				sb.append("/");
			}
		}

		String url = "/Files/Subject/" + sb.toString() + "12_12_Cliton.jpg";
		System.out.println(url);
	}

	public static void mai2(String[] args) {
		System.out.println(StringEscapeUtils
				.unescapeHtml4("c&#417; quan ch&#7913;c n&#259;ng"));
	}

	public static String replace(String value, String returnNewLine,
			String newLine) {
		if (value == null)
			return "";
		return value.replace(returnNewLine, newLine);
	}

	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}
		return s.isEmpty();
	}

	public static boolean isValidEmailAddress(String email) {			
		return isValidEmailAddress(email,null);
	}
	
	public static boolean isValidEmailAddress(String email, String exclude_domains) {		
		if (isEmpty(email)) {
			return false;
		}
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
			if(exclude_domains != null){
				String [] exclude_domainsToks = exclude_domains.split(","); 
				for (String domain : exclude_domainsToks) {
					if(email.endsWith(domain)){
						return false;
					}
				}
				
			}			
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
	
	public static String join(String[] array, String joinStr){
		StringBuilder s = new StringBuilder();
		int l = array.length, n = l -1;
		for (int i=0; i<l;i++) {
			if(i<n){
				s.append(array[i]).append(joinStr);
			} else {
				s.append(array[i]);
			}
		}
		return s.toString();
	}
	public static void main(String[] args) {
		String[] a = new String[3];
		a[0] = "1";
		a[1] = "2";
		a[2] = "3";
		System.out.println(join(a, ","));
		
	}

}
