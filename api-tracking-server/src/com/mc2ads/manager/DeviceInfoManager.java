package com.mc2ads.manager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mc2ads.model.DeviceInfo;
import com.mc2ads.utils.FileUtils;

public class DeviceInfoManager {
	
	static ConcurrentNavigableMap<Long,DeviceInfo> deviceDb;
	static DB mapDb;
	
	static {
		try {
			File dir = new File("mapdb/deviceDb");				
			if(!dir.isFile()){
				dir.createNewFile();				
			}			
			mapDb = DBMaker.newFileDB(dir ).closeOnJvmShutdown().make();
			deviceDb = mapDb.getTreeMap("deviceDb");	
			
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {		
			if(deviceDb == null){
				System.err.println("Can not init deviceDb!");
				System.exit(1);
			}
		}	
	}
	
	public static void importAllDeviceToMapDb(){
		try {						
			String json = FileUtils.loadFilePathToString("user_device_ids.json");
			Type setType = new TypeToken<Collection<DeviceInfo>>() {}.getType();
			Collection<DeviceInfo> deviceInfos = new Gson().fromJson(json, setType);
			for (DeviceInfo deviceInfo : deviceInfos) {
				deviceDb.put(deviceInfo.getId(), deviceInfo);
				System.out.println(deviceInfo.getId() + " "+deviceInfo.getOs_version() + " " + deviceInfo.getDevice_model() + " " + deviceInfo.getDevice_token());	
			}
			mapDb.commit();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {		
			
		}	
	}
	
	
	public static int numberDevices()  {
		return deviceDb.size();
	}
	
	public static int registerNewDeviceInfo(String json)  {
		Type setType = new TypeToken<DeviceInfo>() {}.getType();
		DeviceInfo deviceInfo = new Gson().fromJson(json, setType);
		deviceDb.put(deviceInfo.getId(), deviceInfo);
		mapDb.commit();
		return deviceDb.size();
	}
	
	public static void notifyAllDevices()  {
		
		try {
			Collection<DeviceInfo> deviceInfos = deviceDb.values();
			for (DeviceInfo deviceInfo : deviceInfos) {				
				System.out.println(deviceInfo.getId() + " "+deviceInfo.getOs_version() + " " + deviceInfo.getDevice_model() + " " + deviceInfo.getDevice_token());
				//AndroidGcmUtil.notify(deviceInfo.getDevice_token());
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {		
			
		}		
		
	}

}
