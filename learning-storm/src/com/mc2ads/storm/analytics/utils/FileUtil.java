package com.mc2ads.storm.analytics.utils;

import java.io.File;

public class FileUtil {
	 public static void main(String[] args) 
	 {
	  
	   // Directory path here
	   String path = "libs"; 
	  
	   File folder = new File(path);
	   File[] listOfFiles = folder.listFiles(); 
	  
	   for (int i = 0; i < listOfFiles.length; i++) 
	   {
		   String name = listOfFiles[i].getName();
		   if(name.endsWith(".jar")){
			   StringBuilder s = new StringBuilder("libs/");
			   s.append(name).append(" ; ");
			   System.out.println(s);
		   }	           
	   }
	 }
}
