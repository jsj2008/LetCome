package com.gxq.tpm;

import com.gxq.tpm.mode.IniPatch;
import com.gxq.tpm.tools.Util;
import com.letcome.R;

public class ServiceConfig {
	private final static String KEY_SERVICE_PLATFORM 	= "service_platform";
	private final static String KEY_SERVICE_HQ 			= "service_hq";
	private final static String KEY_SERVICE_H5 			= "service_h5";
	
	private static IniPatch iniPatch;
	
	private static String servicePlatform;
	private static String serviceHq;
	private static String serviceH5;
	
	public static void init(IniPatch patch) {
		iniPatch = patch; 
		
		servicePlatform = getString(KEY_SERVICE_PLATFORM, R.string.service_platform);
		serviceHq = getString(KEY_SERVICE_HQ, R.string.service_hq);
		serviceH5 = getString(KEY_SERVICE_H5, R.string.service_h5);
	}
	
	public static String getServicePlatform() {
		if (servicePlatform != null) {
			return servicePlatform;
		}
		return getString(R.string.service_platform);
	}
	
	public static String getServiceHq() {
		if (serviceHq != null) 
			return serviceHq;
		return getString(R.string.service_hq);
	}
	
	public static String getServiceH5() {
		if (serviceH5 != null) 
			return serviceH5;
		return getString(R.string.service_h5);
	}
	
	private static String getString(String key, int resId) {
		if (iniPatch != null && iniPatch.res_data != null
				&& iniPatch.res_data.get(key) != null) {
			return iniPatch.res_data.get(key);
		}
		return getString(resId);
	}
	
	private static String getString(int resId) {
		return Util.transformString(resId);
	}
	
}
