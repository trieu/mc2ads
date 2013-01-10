package com.mc2ads.server.services;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;
import com.mc2ads.utils.ScalingImageUtil;

@BaseRestHandler(uri = "upload")
public class UploadFilesHandler extends BaseServiceHandler {

	@MethodRestHandler
	public String getServiceName(Map params) {
		return this.getClass().getName();
	}

	@MethodRestHandler
	public String upload(Map params) {
		// to call: request http://localhost:10001/hello/sayHi?name=Trieu Nguyen
		if (request.getMethod().equals("POST")) {
			try {
				fileUploadHandler();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "ok, ";
		} else {
			return "use POST method to upload file";
		}

	}

	private boolean isMultipart;
	private String filePath = "uploaded-files/";
	private int maxFileSize = 5000 * 1024;
	private int maxMemSize = 40 * 1024;
	private File file;

	private void fileUploadHandler() throws ServletException,
			java.io.IOException {
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("D:/data"));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);

		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();
					// Write the file
					if (fileName.lastIndexOf("\\") >= 0) {
						file = new File(
								filePath
										+ fileName.substring(fileName
												.lastIndexOf("\\")));
					} else {
						file = new File(
								filePath
										+ fileName.substring(fileName
												.lastIndexOf("\\") + 1));
					}
					fi.write(file);

					String fileUrl = "file:///" + file.getAbsolutePath();
					int s = 160;
					fileUrl = ScalingImageUtil.scalingImage(fileUrl, s, s);

					System.out.println("Uploaded Filename: " + fileName
							+ "<br>");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex);
		}
	}

}
