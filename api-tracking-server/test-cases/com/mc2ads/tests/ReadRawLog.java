package com.mc2ads.tests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadRawLog {
	
	static int c = 0;
	
	public static String readLines(InputStreamReader in)
	{
	  BufferedReader br = new BufferedReader(in);
	  // you should estimate buffer size
	  StringBuffer sb = new StringBuffer(220000);

	  try
	  {
		  int linesPerRead = 2222;
	    for (int i = 0; i < linesPerRead; ++i)
	    {
	    	String line = br.readLine();
	    	if(line == null) break;
	    	c++;	    	
	    	sb.append(c);
	    	sb.append("\t\t");
	    	sb.append(line);
	    	// placing newlines back because readLine() removes them
	    	sb.append('\n');
	    }
	  }
	  catch (Exception e)
	  {
	    e.printStackTrace();
	  }

	  return sb.toString();
	}
	
	public static void readBigFile(){
		try {
			InputStream inputStream = new FileInputStream("D:/eClick_Log_Report/raw");
			InputStreamReader reader = new InputStreamReader(inputStream);

			String lines = readLines(reader);
			while(lines.length()>0){
				System.out.println(lines);
				lines = readLines(reader);
				   if(c > 50000)
				   {
					   break;
				   }
			}


			reader.close();
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}    
	}
	
	static void readBigFileLog(){
		int c= 0;
		try {			
			String fullpath = "D:/eClick_Log_Report/raw";
			
			BufferedReader br = new BufferedReader(new FileReader(fullpath));
			if(br.ready()){
				String line = null;
				
				while ((line = br.readLine()) != null) {				  
				   c++;
				   System.out.println(c+"\t\t"+line);
//				   if(c> 1000)
//				   {
//					   br.close();
//					   System.exit(1);
//				   }
				}
			}			
			br.close();		
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		System.out.println("### read " + c);
	}

	public static void main(String[] args) {
		long bt = System.currentTimeMillis();
		
		//test
		readBigFile();
		
		long et = System.currentTimeMillis() - bt;
		Date date = new Date(et);
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		System.out.println("\n ----------------------------------- \n ");		
		System.out.println(dateFormatted);
		System.out.println(et);
	}
	
	//07:02:50:469	170469
}
