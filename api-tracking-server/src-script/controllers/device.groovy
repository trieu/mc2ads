import com.mc2ads.manager.DeviceInfoManager;
import com.mc2ads.utils.AndroidGcmUtil;

  
  q = params['q']?.getAt(0) 
  def s = "";
  
  
	if (q == 'number_devices') {
		s += DeviceInfoManager.numberDevices();
	} else if (q == 'registerNewDeviceInfo') {
		String device_info = params['device_info']?.getAt(0) 
		s += DeviceInfoManager.registerNewDeviceInfo(device_info);
	} else if (q == 'importAllDeviceToMapDb') {
		s += DeviceInfoManager.importAllDeviceToMapDb();
	} else if (q == 'notifyAllDevices') {		
		s += DeviceInfoManager.notifyAllDevices();
	} 
  
  
  output = s;