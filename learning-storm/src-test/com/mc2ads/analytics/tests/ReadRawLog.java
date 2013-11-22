package com.mc2ads.analytics.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class ReadRawLog {

	static int c = 0;

	public static String readLines(InputStreamReader in) {
		BufferedReader br = new BufferedReader(in);
		// you should estimate buffer size
		StringBuffer sb = new StringBuffer(220000);

		try {
			int linesPerRead = 2222;
			for (int i = 0; i < linesPerRead; ++i) {
				String sline = br.readLine();
				if (sline == null)
					break;
				c++;
				sb.append(c);
				sb.append("\t\t");
				sb.append(sline);
				// placing newlines back because readLine() removes them
				sb.append('\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static void readBigFile() {
		try {
			InputStream inputStream = new FileInputStream("/data/log-10k.txt");
			InputStreamReader reader = new InputStreamReader(inputStream);

			String lines = readLines(reader);
			while (lines.length() > 0) {
				System.out.println(lines);
				lines = readLines(reader);
				if (c > 3000) {
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

	static void readBigFileLog() {
		int c = 0;
		int maxLineToStop = 10000;
		try {
			String fullpath = "/raw";//TODO

			BufferedReader br = new BufferedReader(new FileReader(fullpath));
			if (br.ready()) {
				String sline = null;
				while ((sline = br.readLine()) != null) {
					c++;
					System.out.println(c + "\t\t" + sline);
					writeToNewFile("/data/log-10k.txt", sline);
					if (c >= maxLineToStop) {
						break;
					}
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

	public static void writeToNewFile(String filename, String sline) {
		try {

			File file = new File(filename);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(sline + "\n");
			bufferWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long bt = System.currentTimeMillis();
		final Stopwatch timer = new Stopwatch().start();

		// test
		readBigFile();
		// readBigFileLog();

		System.out.println("\n ----------------------------------- \n ");
		timer.stop();
		System.out.println("ElapsedTime(seconds): "
				+ (timer.elapsed(TimeUnit.SECONDS)));

		// SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		// String dateFormatted = formatter.format( new Date(diff));
		// System.out.println("\n ----------------------------------- \n ");
		// System.out.println(dateFormatted);
		// System.out.println(diff);
	}

	// 07:02:50:469 170469
}
