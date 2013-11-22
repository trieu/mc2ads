package com.mc2ads.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;

public class GroovyScriptUtil {

	static String path = "src-script";
	static String[] roots = new String[] { path };
	static String controllerPath = "src-script/controllers/";
	static String[] controllerPathRoots = new String[] { controllerPath };	
	
	
	

	static void evalScript() {
		try {
			ClassLoader parent = GroovyScriptUtil.class.getClassLoader();
			GroovyClassLoader loader = new GroovyClassLoader(parent);
			Class groovyClass = loader.parseClass(new File(path
					+ "/SynData.groovy"));

			// let's call some method on an instance
			GroovyObject groovyObject = (GroovyObject) groovyClass
					.newInstance();
			Object[] args2 = {};

			groovyObject.invokeMethod("run", args2);
		} catch (CompilationFailedException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static String runControllerScript(String controllerName, Map<String, String[]> params){
		String output= "";
		try {			
			GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(controllerPathRoots);				
			Binding binding = new Binding();
			binding.setVariable("params", params);
			groovyScriptEngine.run(controllerName+".groovy", binding);
			output = binding.getVariable("output").toString();
		} catch (ResourceException e) {			
			return "controller: " + controllerName + " is not found!";
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return output;
	}

	static void evalByScriptEngine() {
		try {
			String[] roots = new String[] { path };
			GroovyScriptEngine gse = new GroovyScriptEngine(roots);
			Binding binding = new Binding();
			int c = 0;
			while (true) {
				try {
					binding.setVariable("input", (++c) + " world ");
					
					gse.run("HelloScript.groovy", binding);
					System.out.println(binding.getVariable("output"));
					Thread.sleep(2500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		evalByScriptEngine();

	}

}
