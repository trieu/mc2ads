package com.mc2ads;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;
import com.mc2ads.server.BaseLifeCycleListener;
import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.CommonConfigs;
import com.mc2ads.server.ServiceMapperLoader;
import com.mc2ads.utils.FileUtils;
import com.mc2ads.utils.HttpClientUtil;
import com.mc2ads.utils.Log;
import com.mc2ads.utils.ParamUtil;
import com.mc2ads.utils.StringPool;


public class ServiceNodeStarter extends AbstractHandler {
	
	static Logger logger = Logger.getLogger(ServiceNodeStarter.class);
	private static final Map<String, BaseServiceHandler> servicesMap = new HashMap<String, BaseServiceHandler>(50);
	private static final Map<String, String> cachePool = new HashMap<String, String>(100);
	
	private static final CommonConfigs commonConfigs = CommonConfigs.load();
	private static final ServiceMapperLoader mapperLoader = new ServiceMapperLoader();
	static {
		mapperLoader.initClassMapperFromAnnotatedClasses(commonConfigs.getBaseParkageForHandlers());
	}
	private static final BaseLifeCycleListener BASE_LIFE_CYCLE_LISTENER = new BaseLifeCycleListener();
		
	protected static final boolean USE_CACHE = true;
	private static final int BUFFER_SIZE = 10000;
	

	@Override
	public void handle(String target, Request baseRequest, final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException {		
		//TODO logging request here		
		logger.debug("HTTP Method: " + baseRequest.getMethod() + " ,target: " + target);		
		PrintStream writer = null;
		try {
			response.setCharacterEncoding(StringPool.UTF8);
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			
			if(target.equals("/favicon.ico")){		
				response.sendRedirect("/resources/images/favicon.ico");
				return;
			} else if (target.startsWith("/resources/")){		
				processTargetResource(target, request, response);						
				return;
			}	
			
			// response.getWriter().println(request.getRequestURI());
			// response.getWriter().println(request.getQueryString());
			writer = new PrintStream(response.getOutputStream(), true, "UTF-8");	
			if(StringPool.TRUE.equals(request.getParameter("keep-alive")) ){
				//callby: http://localhost:10001/?keep-alive=true&keep-time=10000	
				response.setContentType("text/javascript");				
				int timeSleep = Integer.parseInt(request.getParameter("keep-time"));				
				writer.flush();
				if(timeSleep > 0){
					logger.debug("###connection keep-alive=true in millis: " + timeSleep);
					Thread.sleep(timeSleep);
					writer.print(processStatusData());
				}
				writer.flush();		
			} else {
				processTargetHandler(target, request.getQueryString(), request, response);
			}
		} catch (Exception e) {				
			if(e instanceof java.io.FileNotFoundException){
				String s = target + " is not found";
				logger.error(s);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				if(writer != null) {
					writer.print(s);
					writer.flush();
				}
				return;
			} else {
				e.printStackTrace();
			}
		}
	}
	
	
	public void initLifeCycleListener() {		
		super.addLifeCycleListener(BASE_LIFE_CYCLE_LISTENER);
	}
	
	/**
	 * static resource handler 
	 * 
	 * @param target
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void processTargetResource(String target, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(target.endsWith(".js")||target.endsWith(".css")){					
			String data = cachePool.get(target);
			if(data == null){					
				data = FileUtils.readFileAsString(target);
				if(USE_CACHE) {
					cachePool.put(target,data);
				}
			}	
			if(target.endsWith(".js")){				
				response.setContentType(StringPool.MediaType.JAVASCRIPT);
			} else if(target.endsWith(".css")){	
				response.setContentType(StringPool.MediaType.CSS);
			}
			//System.out.println(data);
			response.getWriter().print(data);
			response.getWriter().flush();
		} else if(target.endsWith("/")){
			File[] files = FileUtils.listFilesInForder(target);
			StringBuilder sb = new StringBuilder();
			sb.append("<ul>");
			for (File file : files) {		
				if( ! file.getName().contains(".svn")){
					String uri = "http://localhost:10001" + target + file.getName();
					String a = "<a href='" + uri + "'>" + uri + "</a>";
					sb.append("<li>").append(a).append("</li>");
				}
			}
			sb.append("</ul>");			
	        response.setContentType(StringPool.MediaType.HTML );
			response.getWriter().print(sb.toString());
			response.getWriter().flush();	        
		} else {
			ServletOutputStream op = response.getOutputStream();
			DataInputStream stream = FileUtils.readFileAsStream(target);
			if(stream != null){
				byte[] bbuf = new byte[BUFFER_SIZE];
				int length = 0, totalLength = 0;
				while ((stream != null) && ((length = stream.read(bbuf)) != -1))
		        {
		            op.write(bbuf,0,length);
		            if(length > 0){
		            	totalLength += length;
		            }
		        }
				stream.close();
		        op.flush();
		        op.close();		      
		        response.setContentType(StringPool.MediaType.OCTET_STREAM);
		        response.setContentLength( totalLength );
			} else {
				response.setStatus(404);
			}
		}
	}


	protected void processTargetHandler(String target, String queryStr, HttpServletRequest request,HttpServletResponse response) throws Exception {
		PrintStream writer = new PrintStream(response.getOutputStream(), true, StringPool.UTF8);
		try {
			String[] toks = target.split("/");
			
			if (toks.length <= 1) {
				logger.error("target must in format [/class-key/method-name/response-type]: "+target);
				return;
			} else if(toks.length == 2){
				if( "admin".equals(toks[1]) ){
					response.sendRedirect("/fosp-crawler/admin/html?path=parser-admin");
					return;
				}	
				toks = new String[] {"",toks[1],"getServiceName","json"};
			}else if(toks.length == 3){
				toks = new String[] {"",toks[1],toks[2],"json"};
			}
			
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			logger.debug("Service handler namespace: "+toks[1]);
			logger.debug("Service handler actionname: "+toks[2]);
						
			String namespace = toks[1];
			Class clazz = mapperLoader.getMapperClass( namespace );			
					
			String className = clazz.getName();

			//check to cache service in pool
			BaseServiceHandler handler = servicesMap.get(className);
			if ( handler == null ) {
				handler = (BaseServiceHandler) clazz.newInstance(); 
				servicesMap.put(className, handler);
			}

			Method method = clazz.getDeclaredMethod(toks[2]);

			Object result = "";
			try {
				//inject req + res into the service
				handler.setHttpServletRequest(request);
				handler.setHttpServletResponse(response);
				result = method.invoke(servicesMap.get(className));
			} catch (Throwable e1) {
				//e1.printStackTrace();
				result = e1.getCause().getClass().getName() + ":" + e1.getMessage();
				writer.print(result);
				return;
			}
			//logger.info(result);
			
			
			String responseType = toks[3].toLowerCase();
			response.setCharacterEncoding(StringPool.UTF8);
			logger.info(responseType);
			if(responseType.equals("json")){
				Gson gson = new Gson();	
				response.setContentType(StringPool.MediaType.JSON);
				if(toks[2].equals("getServiceName")){
					Map<String, String> obj = new HashMap<String, String>(2);
					obj.put("service-name", result.toString());
					writer.print(gson.toJson(obj));
				} else {
					writer.print(gson.toJson(result));
				}
			} else if(responseType.equals("html")){
				//logger.info(gson.toJson(result));	
				response.setContentType(StringPool.MediaType.HTML);
				
				String path = ParamUtil.getString(request, "path","");
				if(path.length()>5){
					String filepath = "/resources/html/"+ path +".html";			
					String html = "404 not found!";
					try {
						html = FileUtils.readFileAsString(filepath);					
					} catch (IOException e) {					
						e.printStackTrace();
					}
					writer.print(html);
				} else {
					writer.print(result);
				}
			} else if(responseType.equals("string")){			
				response.setContentType(StringPool.MediaType.HTML);
				writer.print(result);
			} else {
				String filepath = "/resources/html/target_response.html";			
				String html = "";
				try {
					html = FileUtils.readFileAsString(filepath);
				} catch (IOException e) {
					if(e instanceof java.io.FileNotFoundException){
						logger.error(filepath + " is found");
					} else {
						e.printStackTrace();
					}
				}		
				if(html != null){
					html = html.replace("{Origin}", request.getHeader("Origin"));
					html = html.replace("{json}", new Gson().toJson(result));
					writer.print(html);
				} else {
					writer.print("Can not handle the request: " + target);
				}
			}
			writer.flush();
		} catch (java.lang.NoSuchMethodException e) {
			writer.print("Not found handler for the target: " + target + " !");
		} catch (java.lang.IllegalArgumentException e) {
			writer.print("wrong number of arguments for the target: " + target + " !");
		} catch (Exception e) {	
			if(e instanceof java.lang.reflect.InvocationTargetException){
				Throwable c = e.getCause();
				//System.err.println(c.getClass().getName());
				c.printStackTrace();
			} else {
				e.printStackTrace();
			}
			writer.print(e.getMessage());
		}
	}
	
	protected String processStatusData() {
		return "console.log('"+(new Date()).toString() + "'); ";
	}
	
	public static void main(String[] args) throws Exception {	
		// Set up a simple configuration that logs on the console.
	    //BasicConfigurator.configure();
	    Log.setPropertyConfiguratorLog4J("");
		int port = commonConfigs.getPort();
		if(args.length == 1){
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {}
		}
		String html = HttpClientUtil.executeGet("http://127.0.0.1:"+port);
		if("444".equals(html)){
			Server server = new Server(port);
			
			
//			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//	        context.setContextPath("/");
//	        server.setHandler(context);
//	 
//	        context.addServlet(new ServletHolder(new ApiRouterServlet()),"/api/*");
	        
			
			ServiceNodeStarter theHandler = new ServiceNodeStarter();
			theHandler.initLifeCycleListener();		
			server.setHandler(theHandler);
			Log.get(ServiceNodeStarter.class).info("Starting Emdeded Jetty "+ Server.getVersion() +" at port " + port + " ...");
			logger.info("JVM: " + System.getProperty("sun.arch.data.model") + " bit, version: " + System.getProperty("java.version"));
			server.start();
			server.join();		
		} else {
			System.err.println("\n#### An agent is listening on port "+port + " ####\n");
		}
	}	

}
