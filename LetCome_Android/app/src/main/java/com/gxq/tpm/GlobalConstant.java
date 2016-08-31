package com.gxq.tpm;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

/**
 * 协议类型
 * product_type.0 = "框架"
 * product_type.1 = "A股"
 * product_type.2 = "期指"
 * product_type.3 = "沪金"
 * product_type.4 = "沪银"
 * product_type.5 = "注销"
 * product_type.6 = "实盘券"
 * product_type.7 = "基金"
 * product_type.8 = "A股递延协议"
 * product_type.9 = "分级B递延协议"
 * product_type.10 = "A50交易协议"
 * ;产品合约类型（暂时支持1-100）
 * article.product_type.1 = "A股"
 * article.product_type.2 = "期指"
 * article.product_type.3 = "沪金"
 * article.product_type.4 = "沪银"
 * article.product_type.5 = "A股递延规则"
 * article.product_type.6 = "A股禁买说明"
 * article.product_type.7 = "可提现金额提示"
 * article.product_type.8 = "基金禁买说明"
 * article.product_type.9 = "基金合约"
 * article.product_type.10 = "基金递延规则"
 * article.product_type.11 = "实盘券合约"
 * article.product_type.50 = "A50合约"
 */
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
