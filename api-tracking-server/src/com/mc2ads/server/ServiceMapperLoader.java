package com.mc2ads.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.utils.AnnotationUtils;
import com.mc2ads.utils.FileUtils;



public class ServiceMapperLoader {
	private static final Map<String,Class> classMapper = new HashMap<String, Class>();

	
	
	public ServiceMapperLoader() {
		
	}
	
	public Class getMapperClass(String namespace) throws ClassNotFoundException {
		Class clazz = classMapper.get(namespace);
		if(clazz != null){
			return clazz;
		}		
		throw new RuntimeException("Wrong config mapper syntax!");
	}
	
	public void initClassMapperFromAnnotatedClasses(List<String> baseParkageForHandlers){
		int c = 0;
		List<String> jarFiles = FileUtils.listAllFilesInRunntimeDir(".jar");
		try {
			for (String baseParkage : baseParkageForHandlers) {
				
				List<Class> classes = AnnotationUtils.getClasses(baseParkage);
				for (Class clazz : classes) {
					BaseRestHandler baseRestHandler = (BaseRestHandler) clazz.getAnnotation(BaseRestHandler.class);
					if(baseRestHandler != null){
						
						String uri = baseRestHandler.uri();
						if(uri !=  null){
							classMapper.put(uri, clazz);
							System.out.println("mapped \""+baseRestHandler.uri() + "\" => " + clazz.getName());
							c++;
						}						
					}
				}
				
				for (String jarFile : jarFiles) {
					jarFile = "file:///" + jarFile;				
					
					List<Class> classes2 = AnnotationUtils.findClasses(jarFile,baseParkage);
					for (Class clazz : classes2) {
						BaseRestHandler baseRestHandler = (BaseRestHandler) clazz.getAnnotation(BaseRestHandler.class);
						if(baseRestHandler != null){
							
							String uri = baseRestHandler.uri();
							if(uri !=  null && !classMapper.containsKey(uri) ){
								classMapper.put(uri, clazz);
								System.out.println("mapped \""+baseRestHandler.uri() + "\" => " + clazz.getName());
								c++;
							}						
						}
					}
				}
			}
		} catch (Exception e) {				
			e.printStackTrace();
		}
		
		
		System.out.println("initClassMapperFromAnnotatedClasses: " + c);
	}
	
	
}
