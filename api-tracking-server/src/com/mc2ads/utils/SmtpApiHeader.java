package com.mc2ads.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Sendgrid SMTP API for Java
 * Java port of Sendgrid's PHP SmtpApiHeader library
 * (see http://docs.sendgrid.com/documentation/api/smtp-api/php-example/)
 * More at http://sendgrid.com/docs/API%20Reference/SMTP%20API/apps.html
 *
 * @author Jan Hohner (http://www.janhohner.de)
 */
public class SmtpApiHeader {

	private HashMap<String, Object> json_obj = new HashMap<String, Object>();

	public void addTo(LinkedList<String> recipients)
	{
		LinkedList<String> s;
		if ( ! json_obj.containsKey("to"))
			s = new LinkedList<String>();
		else
			s = (LinkedList<String>)json_obj.get("to");

		s.addAll(recipients);

		json_obj.put("to", s);
	}

	public void addSubVal(String varname, LinkedList<String> val)
	{
		HashMap<String, LinkedList<String>> s;

		if ( ! json_obj.containsKey("sub"))
			s = new HashMap<String, LinkedList<String>>();
		else
			s = (HashMap<String, LinkedList<String>>)json_obj.get("sub");

		LinkedList<String> ss;
		if ( ! s.containsKey(varname))
			ss = new LinkedList<String>();
		else
			ss = s.get(varname);

		ss.addAll(val);
		s.put(varname, ss);
		json_obj.put("sub", s);
	}

	public void setUniqueArgs(HashMap<String, String> vals)
	{
		HashMap<String, String> s;

		if ( ! json_obj.containsKey("unique_args"))
			s = new HashMap<String, String>();
		else
			s = (HashMap<String, String>)json_obj.get("unique_args");

		s.putAll(vals);
		json_obj.put("unique_args", s);
	}

	public void setCategory(String cat)
	{
		json_obj.put("category", cat);
	}

	public void addFilterSetting(String filter, String setting, String value)
	{
		HashMap<String, HashMap<String, HashMap<String, String>>> s;
		if ( ! json_obj.containsKey("filters"))
			s = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		else
			s = (HashMap<String, HashMap<String, HashMap<String, String>>>)json_obj.get("filters");

		HashMap<String, HashMap<String, String>> ss;
		if ( ! s.containsKey(filter))
			ss = new HashMap<String, HashMap<String, String>>();
		else
			ss = s.get(filter);

		HashMap<String, String> sss;
		if ( ! ss.containsKey("settings"))
			sss = new HashMap<String, String>();
		else
			sss = ss.get("settings");

		sss.put(setting, value);
		ss.put("settings", sss);
		s.put(filter, ss);
		json_obj.put("filters", s);
	}

	public String asJSON()
	{
		ObjectMapper m = new ObjectMapper();

		try
		{
			String json = m.writeValueAsString(json_obj);
			json = json.replace("\":[", "\": [");
			json = json.replace("\",\"", "\", \"");
			json = json.replace("],\"", "], \"");
			json = json.replace("\":{", "\": {");
			json = json.replace("},\"", "}, \"");
			json = json.replace("{{[", "{{ [");
			json = json.replace("]}}", "] }}");
			return wordWrap(json, 76);
		}
		catch (IOException e)
		{
			return null;
		}
	}

	// from http://ramblingsrobert.wordpress.com/2011/04/13/java-word-wrap-algorithm/
	private static String wordWrap(String in, int length) {
		String newline = '\n'+" ";
		//:: Trim
		while(in.length() > 0 && (in.charAt(0) == '\t' || in.charAt(0) == ' '))
			in = in.substring(1);

		//:: If Small Enough Already, Return Original
		if(in.length() < length)
			return in;

		//:: If Next length Contains Newline, Split There
		if(in.substring(0, length).contains(newline))
			return in.substring(0, in.indexOf(newline)).trim() + newline +
					wordWrap(in.substring(in.indexOf("\n") + 1), length);

		//:: Otherwise, Split Along Nearest Previous Space/Tab/Dash
		int spaceIndex = Math.max(Math.max( in.lastIndexOf(" ", length),
				in.lastIndexOf("\t", length)),
				in.lastIndexOf("-", length));

		//:: If No Nearest Space, Split At length
		if(spaceIndex == -1)
			spaceIndex = length;

		//:: Split
		return in.substring(0, spaceIndex).trim() + newline + wordWrap(in.substring(spaceIndex), length);
	}
}