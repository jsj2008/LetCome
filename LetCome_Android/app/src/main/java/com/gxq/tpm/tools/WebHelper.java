package com.gxq.tpm.tools;

import com.gxq.tpm.network.RequestInfo;

public class WebHelper {

	private WebHelper() {}
	
	
	public static WebParams addParam(String url) {
		return new WebParams(url);
	}
	
	public static WebParams addParam(RequestInfo info) {
		return new WebParams(info.getUrl());
	}

	public static WebParams addParam(String url, String key, String value) {
		return new WebParams(url).addParam(key, value);
	}
	
	public static WebParams addParam(RequestInfo info, String key, String value) {
		return new WebParams(info.getUrl()).addParam(key, value);
	}
	
	public static class WebParams {
		public final static String MSGID 	= "msgId";
		public final static String USETYPE 	= "useType";
		public final static String VALID 	= "isvalid";
		public final static String TYPE 	= "type";
		public final static String AID 		= "aid";
		public final static String ID 		= "id";
		public final static String HID 		= "history_id";
		public final static String APPTYPE	= "appType"; // 2带签署按钮
		public final static String CID 		= "cid";
		public final static String CHANNEL	= "channel_id";
				
		private String mUrl;
		private String mParams;
		
		public WebParams(String url) {
			this.mUrl = url;
		}
		
		public WebParams addParam(String key, String value) {
			if (mParams == null || mParams.length() == 0) {
				mParams = "?" + key + "=" + value;
			} else {
				mParams += "&" + key + "=" + value;
			}
			return this;
		}
		
		public WebParams addParam(String key, long value) {
			return addParam(key, Long.toString(value));
		}

		public String getUrl() {
			return mUrl + mParams;
		}
	}
	
}
