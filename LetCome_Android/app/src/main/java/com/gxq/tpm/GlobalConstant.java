package com.gxq.tpm;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

public class GlobalConstant {
	public static final String PRODUCT_ALL			= "all";
	
	public static final int PRODUCT_TYPE_PLATFORM 	= 0;
	
	public static final int USER_TYPE_STRATEGY 		= 1;

	public static final String STOCK_HQ_TYPE		= "SDBF";
	
	public static final int TOAST_OFFSET_Y = 60;//浮层提示偏移量
	
	public static final boolean isEncrypt=Util.transformBoolean(R.bool.isEncrypt);//密码加密开关
	
	public static final int SCHEME_REFRESH_TIME = 845; // 方案刷新时间  
	
	public static final String p_type = "";
	
	// 跳转外部网页
	public static final String EXTERN_BROWSER = "extern_browser";
}
