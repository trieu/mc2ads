package com.mc2ads.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.imageio.ImageIO;

public class ScalingImageUtil {
	final static int BUFFER_SIZE = 2048;

	public static String fileUrl(String imageUrl, String localFileName,
			String destinationDir) {
		OutputStream outStream = null;
		URLConnection uCon = null;
		String scaledFilePath = destinationDir + localFileName;
		InputStream is = null;
		try {
			byte[] buf;
			int ByteRead, ByteWritten = 0;
			URL url = new URL(imageUrl);
			outStream = new BufferedOutputStream(new FileOutputStream(
					scaledFilePath));

			uCon = url.openConnection();
			is = uCon.getInputStream();
			buf = new byte[BUFFER_SIZE];
			while ((ByteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, ByteRead);
				ByteWritten += ByteRead;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Processing ..." + scaledFilePath);
		return scaledFilePath;
	}

	public static String buildFilenameForSize(String fullFilePath, int w,
			int h, String destinationDir) {
		if (fullFilePath == null) {
			throw new IllegalArgumentException(
					"filePath or destinationDir must not NULL");
		}
		fullFilePath = fullFilePath.replace("\\", "/");

		int slashIndex = fullFilePath.lastIndexOf('/');
		int periodIndex = fullFilePath.lastIndexOf('.');

		String fileName = fullFilePath.substring(slashIndex + 1, periodIndex);
		if (periodIndex >= 1 && slashIndex >= 0
				&& slashIndex < fullFilePath.length() - 1) {
			StringBuilder sb = new StringBuilder(fileName);
			sb = sb.append("-").append(w).append("x").append(h).append(".")
					.append(fullFilePath.substring(periodIndex + 1));
			return fileUrl(fullFilePath, sb.toString(), destinationDir);
		} else {
			throw new IllegalArgumentException("get file name fail");
		}
	}

	public static String fileDownloadFromURL(String filePath,
			String destinationDir) {
		if (filePath == null || destinationDir == null) {
			throw new IllegalArgumentException(
					"filePath or destinationDir must not NULL");
		}
		filePath = filePath.replace("\\", "/");

		int slashIndex = filePath.lastIndexOf('/');
		int periodIndex = filePath.lastIndexOf('.');

		String fileName = filePath.substring(slashIndex + 1);
		if (periodIndex >= 1 && slashIndex >= 0
				&& slashIndex < filePath.length() - 1) {
			return fileUrl(filePath, fileName, destinationDir);
		} else {
			System.err.println("path or file name.");
		}
		return "";
	}

	static String THUMB_DIR = "D:/";

	public static String scalingImage(String fullPath, int w, int h) {
		try {
			String scaledPath = buildFilenameForSize(fullPath, w, h, THUMB_DIR);
			System.out.println(scaledPath);

			BufferedImage image = ImageIO.read(new File(scaledPath));
			image = ImageUtil.blurImage(image);

			BufferedImage newImage = ImageUtil.scaleImage(image, w, h);
			ImageIO.write(newImage, "JPG", new File(scaledPath));
			image.flush();
			newImage.flush();
			image = null;
			newImage = null;
			return scaledPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fullPath;
	}

	public static void scaleByPath(String[] args) {
		if (args.length != 1) {
			System.err.println("Missing photo Path param!");
			return;
		}
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("image-scaler.properties"));
			THUMB_DIR = properties.getProperty("thumbnail.folder", "");
			String[] sizes = properties.getProperty("thumbnail.sizes", "").split(",");
			String fileUrl = args[0];
			for (String size : sizes) {
				int s = Integer.parseInt(size);
				scalingImage(fileUrl, s, s);
			}						
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {

	    File folder = new File("D:/PHOTOS/100EOS5D");
	    THUMB_DIR = "D:/PHOTOS/100EOS5D/thumbs/";
	    int s = 800;
	    
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	        	name = name.toLowerCase();
	        //	System.out.println(name);
	            int i = name.indexOf(".");
	            if(i<=0){
	            	return false;
	            }
	            
	            String ext = name.substring(i+1);
	        //    System.out.println(ext);
	            return ext.equals("jpg");
	        }
	    };
	    
	    File[] listOfFiles = folder.listFiles(filter);

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	
			String fileUrl = "file:///"+listOfFiles[i].getAbsolutePath();
			fileUrl =scalingImage(fileUrl, s, s);
	     
	        System.out.println("DONEFile " + fileUrl);
	     
	    }
	  }
}