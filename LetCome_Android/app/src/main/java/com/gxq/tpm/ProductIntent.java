package com.gxq.tpm;

public class ProductIntent {
	
	public final static String EXTRA_WHICH_FRAGMENT 		= "com.gxq.tpm.extra.WHICH_FRAGMENT";
	
	public final static String EXTRA_REFRESH 				= "com.gxq.tpm.extra.REFRESH";
	public final static int REFRESH_NEW_BUYING				= 0x0001; // 
	public final static int REFRESH_BUYING					= 0x0002; //
	public final static int REFRESH_NEW_NOTICING			= 0x0004; // 
	public final static int REFRESH_NEW_SELLING				= 0x0010; // 
	public final static int REFRESH_SELLING					= 0x0020; //
	public final static int REFRESH_NEW_ACCOUNT				= 0x0040; //
	
	public final static String EXTRA_STOCK					= "com.gxq.tpm.extra.STOCK";

	public final static String EXTRA_STOCK_CODE				= "com.gxq.tpm.extra.STOCK_CODE";
	
	public final static String EXTRA_UNSIGN_AGREEMENT 		= "com.gxq.tpm.extra.UNSIGN_AGREEMENT";
	
	public final static int UNSIGN_AGREEMENT_SIGN			= 0;
	
	public final static int UNSIGN_AGREEMENT_VIEW			= 1;
	
	public final static String EXTRA_HANDICAP				= "com.gxq.tpm.extra.HANDICAP";
	
	public final static String EXTRA_TITLE					= "com.gxq.tpm.extra.TITLE";
	
	public final static String EXTRA_URL					= "com.gxq.tpm.extra.URL";
	
	public final static String EXTRA_NEED_SESSION			= "com.gxq.tpm.extra.NEED_SESSION";
	
	public final static String EXTRA_HTTP_REQUEST			= "com.gxq.tpm.extra.HTTP_REQUEST";
	public final static int REQUEST_POST					= 1;
	public final static int REQUEST_GET						= 2;
	
	public final static String EXTRA_FINISH_DIRECT			= "com.gxq.tpm.extra.FINISH_DIRECT";
	
	public final static String EXTRA_CLEAR_CACHE			= "com.gxq.tpm.extra.CLEAR_CACHE";
	
	public final static String EXTRA_ARTICLE_ID				= "com.gxq.tpm.extra.ARTICLE_ID";
	
	public final static String EXTRA_ARTICLE_NAME			= "com.gxq.tpm.extra.ARTICLE_NAME";
	
	public final static String EXTRA_SWITCH_ID				= "com.gxq.tpm.extra.SWITCH_ID";
	
	// 跳转时使用
	public final static String EXTRA_ACTIVITY_FROM			= "com.gxq.tpm.extra.ACTIVITY_FROM";
	// 用于登录页跳转
	public final static int FROM_LOGIN						= 1;
	// 从launch界面跳转
	public final static int FROM_LAUNCH						= 2;
	// 从超时处跳转
	public final static int FROM_SESSION_OUT				= 3;
	//从设置手机号登陆成功后跳转
	public final static int FROM_MINE_RESET_PWD				= 4;
	
	public final static String EXTRA_MOBILE					= "com.gxq.tpm.extra.MOBILE";
	
	public final static String EXTRA_DETAIL					= "com.gxq.tpm.extra.DETAIL";
	
	public final static String EXTRA_INSP_PROD				= "com.gxq.tpm.extra.INSP_PROD";
	
	public final static String EXTRA_TYPE					= "com.gxq.tpm.extra.TYPE";
	
	public final static String EXTRA_NICK_NAME				= "com.gxq.tpm.extra.NICK_NAME";
	
	public final static String EXTRA_RESET_NICK_NAME		= "com.gxq.tpm.extra.RESET_NICK_NAME";
	
	public final static String EXTRA_NEED_NOTICE			= "com.gxq.tpm.extra.NEED_NOTICE";
	
	public final static String ACTION_EXIT					= "com.gxq.tpm.comm.ExitApp";
	
	public final static String ACTION_FINSH_MODIFY_PHONEACT	= "com.gxq.tpm.comm.Finshmodifyphonenum";
	
	public final static String EXTRA_SHOW_CANCEL			= "com.gxq.tpm.extra.SHOW_CANCEL";
	
	public final static String EXTRA_ID						= "com.gxq.tpm.extra.ID";
	
	public final static String EXTRA_DETIAL_PID				= "com.gxq.tpm.extra.DETIAL_PID";
	
	public final static String EXTRA_DETIAL_TYPE			= "com.gxq.tpm.extra.DETIAL_TYPE";
	
	public final static String EXTRA_STRATEGY				= "com.gxq.tpm.extra.EXTRA_STRATEGY";
	
	public final static String EXTRA_SEELEMENT              = "com.gxq.tpm.extra.EXTRA_SEELEMENT";
	
	public final static String EXTRA_VALUE					= "com.gxq.tpm.extra.EXTRA_VALUE";
	
	public final static String EXTRA_INTENT					= "com.gxq.tpm.extra.EXTRA_INTENT";
	
	public final static String EXTRA_ENABLE					= "com.gxq.tpm.extra.EXTRA_ENABLE";
	
	public final static String EXTRA_TEXT					= "com.gxq.tpm.extra.EXTRA_TEXT";
	
	public final static String EXTRA_POLICY					= "com.gxq.tpm.extra.EXTRA_POLICY";
	
	public final static String EXTRA_SCHEME_DETAIL			= "com.gxq.tpm.extra.EXTRA_SCHEME_DETAIL";
	
	public final static String EXTRA_STOCK_TOTAL			= "com.gxq.tpm.extra.EXTRA_STOCK_TOTAL";
}
