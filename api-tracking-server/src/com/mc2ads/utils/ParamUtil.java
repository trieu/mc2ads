package com.mc2ads.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParamUtil {

    // Servlet Request

    public static boolean getBoolean(HttpServletRequest request, String param) {
        return GetterUtil.getBoolean(request.getParameter(param));
    }

    public static boolean getBoolean(
        HttpServletRequest request, String param, boolean defaultValue) {

        return get(request, param, defaultValue);
    }

    public static boolean[] getBooleanValues(
        HttpServletRequest request, String param) {

        return getBooleanValues(request, param, new boolean[0]);
    }

    public static boolean[] getBooleanValues(
        HttpServletRequest request, String param, boolean[] defaultValue) {

        return GetterUtil.getBooleanValues(
            request.getParameterValues(param), defaultValue);
    }

    public static Date getDate(
        HttpServletRequest request, String param, DateFormat df) {

        return GetterUtil.getDate(request.getParameter(param), df);
    }

    public static Date getDate(
        HttpServletRequest request, String param, DateFormat df,
        Date defaultValue) {

        return get(request, param, df, defaultValue);
    }

    public static double getDouble(HttpServletRequest request, String param) {
        return GetterUtil.getDouble(request.getParameter(param));
    }

    public static double getDouble(
        HttpServletRequest request, String param, double defaultValue) {

        return get(request, param, defaultValue);
    }

    public static double[] getDoubleValues(
        HttpServletRequest request, String param) {

        return getDoubleValues(request, param, new double[0]);
    }

    public static double[] getDoubleValues(
        HttpServletRequest request, String param, double[] defaultValue) {

        return GetterUtil.getDoubleValues(
            request.getParameterValues(param), defaultValue);
    }

    public static float getFloat(HttpServletRequest request, String param) {
        return GetterUtil.getFloat(request.getParameter(param));
    }

    public static float getFloat(
        HttpServletRequest request, String param, float defaultValue) {

        return get(request, param, defaultValue);
    }

    public static float[] getFloatValues(
        HttpServletRequest request, String param) {

        return getFloatValues(request, param, new float[0]);
    }

    public static float[] getFloatValues(
        HttpServletRequest request, String param, float[] defaultValue) {

        return GetterUtil.getFloatValues(
            request.getParameterValues(param), defaultValue);
    }

    public static int getInteger(HttpServletRequest request, String param) {
        return GetterUtil.getInteger(request.getParameter(param));
    }
    
    public static int getInt(
            HttpServletRequest request, String param, int defaultValue) {

            return get(request, param, defaultValue);
        }

    public static int getInteger(
        HttpServletRequest request, String param, int defaultValue) {

        return get(request, param, defaultValue);
    }

    public static int[] getIntegerValues(
        HttpServletRequest request, String param) {

        return getIntegerValues(request, param, new int[0]);
    }

    public static int[] getIntegerValues(
        HttpServletRequest request, String param, int[] defaultValue) {

        return GetterUtil.getIntegerValues(
            request.getParameterValues(param), defaultValue);
    }

    public static long getLong(HttpServletRequest request, String param) {
        return GetterUtil.getLong(request.getParameter(param));
    }

    public static long getLong(
        HttpServletRequest request, String param, long defaultValue) {

        return get(request, param, defaultValue);
    }

    public static long[] getLongValues(
        HttpServletRequest request, String param) {

        return getLongValues(request, param, new long[0]);
    }

    public static long[] getLongValues(
        HttpServletRequest request, String param, long[] defaultValue) {

        return GetterUtil.getLongValues(
            request.getParameterValues(param), defaultValue);
    }

    public static short getShort(HttpServletRequest request, String param) {
        return GetterUtil.getShort(request.getParameter(param));
    }

    public static short getShort(
        HttpServletRequest request, String param, short defaultValue) {

        return get(request, param, defaultValue);
    }

    public static short[] getShortValues(
        HttpServletRequest request, String param) {

        return getShortValues(request, param, new short[0]);
    }

    public static short[] getShortValues(
        HttpServletRequest request, String param, short[] defaultValue) {

        return GetterUtil.getShortValues(
            request.getParameterValues(param), defaultValue);
    }

    public static String getString(HttpServletRequest request, String param) {
        return GetterUtil.getString(request.getParameter(param));
    }

    public static String getString(
        HttpServletRequest request, String param, String defaultValue) {

        return get(request, param, defaultValue);
    }

    public static boolean get(
        HttpServletRequest request, String param, boolean defaultValue) {

        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static Date get(
        HttpServletRequest request, String param, DateFormat df,
        Date defaultValue) {

        return GetterUtil.get(request.getParameter(param), df, defaultValue);
    }

    public static double get(
        HttpServletRequest request, String param, double defaultValue) {

        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static float get(
        HttpServletRequest request, String param, float defaultValue) {

        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static int get(
        HttpServletRequest request, String param, int defaultValue) {

        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static long get(
        HttpServletRequest request, String param, long defaultValue) {

        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static short get(
        HttpServletRequest request, String param, short defaultValue) {

        return GetterUtil.get(request.getParameter(param), defaultValue);
    }

    public static String get(
        HttpServletRequest request, String param, String defaultValue) {

        String returnValue =
            GetterUtil.get(request.getParameter(param), defaultValue);

        if (returnValue != null) {
            return returnValue.trim();
        }

        return null;
    }
    
    public static JSONArray getJsonArray(HttpServletRequest request, String param){
    	String jsontext = getString(request, param, "[]");
    	try {
			return new JSONArray(jsontext);
		} catch (JSONException e) {			
		}
    	return new JSONArray();
    }
    
    public static JSONObject getJsonObject(HttpServletRequest request, String param){
    	String jsontext = getString(request, param, "[]");
    	try {
			return new JSONObject(jsontext);
		} catch (JSONException e) {			
		}
    	return new JSONObject();
    }

    public static void print(HttpServletRequest request) {
        Enumeration<String> enu = request.getParameterNames();

        while (enu.hasMoreElements()) {
            String param = enu.nextElement();

            String[] values = request.getParameterValues(param);

            for (int i = 0; i < values.length; i++) {
                System.out.println(param + "[" + i + "] = " + values[i]);
            }
        }
    }

}
