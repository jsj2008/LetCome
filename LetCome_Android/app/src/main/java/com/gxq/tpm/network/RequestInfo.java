package com.gxq.tpm.network;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ServiceConfig;

enum Link {PLATFORM, HQ, H5}

enum Type {POST, GET}

enum Certify {YES, NO}

public enum RequestInfo {
	// 用户 -- 登录相关接口
	LOGIN("/user/login", Link.PLATFORM, Type.POST, Certify.YES, "请求登录"),
	GET_VERIFY_IMG("/user/getsecuritycodePic", Link.PLATFORM, "获取登陆验证图片"),
	CHECK_VERIFY("/user/verifysecuritycode", Link.PLATFORM, Type.POST, Certify.YES, "验证码校验"),
	GET_MOBILE_CODE("/user/sendMobileCode", Link.PLATFORM, Type.POST, Certify.NO, "发送手机验证码"),
	FORGET_MOBILE_CODE_GET("/mobile/sendmobilecodeforpwd", Link.PLATFORM, Type.POST, Certify.NO, "找回密码用户手机验证码发送"),
	FORGET_MOBILE_CHECK("/mobile/mobilecodecheckforpwd",	Link.PLATFORM, Type.POST, Certify.NO, "找回密码用户手机验证码校验"),
	CHANGE_PWD("/user/changepassword", Link.PLATFORM, Type.POST, Certify.YES, "用户修改密码"),
	LOGOUT("/user/logoff", Link.PLATFORM, Type.POST, Certify.YES, "退出登录"),
	FORGET_RESET_PWD("/user/resetPassword",	Link.PLATFORM, Type.POST, Certify.NO, "找回密码用户重置密码"),
	OLD_SET_PWD_NICK("/user/mobileRegisterPawNic", Link.PLATFORM, Type.POST, Certify.NO, "手机(老用户注册)-设置安全密码和昵称"),
	PWD_CHECK("/user/checkpassword", Link.PLATFORM, Type.POST, Certify.NO, "密码校验"),
//	LOGIN_BY_SSO("/ssologin/loginbysso", Link.PLATFORM, Type.POST, Certify.YES, "操盘宝授权登录"),
	RESET_PWD("/user/resetpasswordmask", Link.PLATFORM, Type.POST, Certify.YES, "绑定新手机重置密码"),
	
	// 用户 -- 注册接口
	MOBILE_REGISTER_CHECK("/user/mobileCodeRegCheck", Link.PLATFORM, Type.POST, Certify.YES, "手机验证码校验(老用户注册)"),
	MOBILE_REGISTER("/user/mobileRegister", Link.PLATFORM, Type.POST, Certify.YES, "手机注册"),
	OLD_SET_PWD("/user/mobileRegisterPaw", Link.PLATFORM, Type.POST, Certify.YES, "手机(老用户注册)-设置安全密码"),
	SET_MOBILE_AND_PASSWORD("mobile/set_moblie_and_password", Link.PLATFORM, Type.POST, Certify.YES, "绑定手机与密码"),
	
	// 用户 -- 用户信息接口
	USER_INFO("/user/getuserinfo", Link.PLATFORM, Type.POST, Certify.YES, "获取用户信息"),
	SET_NICKNAME("/user/setnickname", Link.PLATFORM, Type.POST, Certify.YES, "设置昵称"),
	PROD_QUERY("/user/prodQuery", Link.PLATFORM, "查询操盘宝 余额"),
	
	// 用户 -- 站内信接口
	MSG_LIST("/message/getmessagelist", Link.PLATFORM, Type.GET, Certify.YES, "站内信列表"),
	MSG_PREPARE("/message/prereadmassmsg", Link.PLATFORM, Type.GET, Certify.YES, "站内信预处理 /*登录后*/"),
	MSG_CONTENT("/message/getmsgcontent", Link.PLATFORM, Type.GET, Certify.YES, "站内信查询"),
	MSG_POPUP_MESSAGE("message/getpopupmsg", Link.PLATFORM, Type.GET, Certify.YES, "获取最新弹框消息"),
	MSG_READ_MSG("message/readmsg", Link.PLATFORM, Type.GET, Certify.YES, "标记消息为已读"),
	MSG_NEED_NOTICE("message/neednoticecnt", Link.PLATFORM, Type.POST, Certify.YES, "获取未读提醒的记录数量"),
	
	// 用户 -- 协议接口
	PROTOCOL_UNSIGNED("/agreement/getuserunsignedagreement", Link.PLATFORM, Type.GET, Certify.YES, "获取未签署协议列表"),
	PROTOCOL_SIGN("/agreement/signagreement", Link.PLATFORM, Type.POST, Certify.YES, "用户签署协议"),
	PROTOCOL_CONTENT("/agreement/getagreementinfo", Link.PLATFORM, Type.POST, Certify.YES, "获取用户协议信息"),
	P_ARTICLE_LIST("/agreement/articlelist", Link.PLATFORM, Type.POST, Certify.YES, "获取协议合约列表"),
	P_ARTICLE_CONTENT("/agreement/articlecontent", Link.PLATFORM, Type.POST, Certify.YES, "获取协议合约内容"),
	
	AGREEMENT_SIGNED_LIST("agreement/signedarticlelist", Link.PLATFORM, Type.POST, Certify.YES, "已签协议合约（交易合作协议）列表取得"),
	AGREEMENT_USERAGREEMENT_LIST("agreement/useragreementlist", Link.PLATFORM, Type.POST, Certify.YES, "协议列表（含已签的旧版本协议）"),
	AGREEMENT_ARTICLE_SAMPLE_LIST("agreement/articlesamplelist",  Link.PLATFORM, Type.POST, Certify.YES, "合约模板展示"),
	
	// 用户 -- 额度接口
	DOUBLE_LIMIT("/quota/increasequota", Link.PLATFORM, Type.POST, Certify.YES, "用户配额加倍申请"),
	
	// 用户 -- 我的账户
	MYACCOUNT_MOUNT_URL("/myaccount/getmyaccountamount", Link.PLATFORM, Type.POST, Certify.YES, "积分账号余额"),
	
	//手机绑定
	MOBILE_SEND_CODE_FOR_AUTH("/mobile/sendmobilecodeforauth", Link.PLATFORM, Type.POST, Certify.YES, "向原手机号发送验证码"), 
	MOBILE_CHECK_MOBILE_CODE("/mobile/checkmobilecode", Link.PLATFORM, Type.POST, Certify.YES, "原手机验证码校验（重绑手机号）"),
	MOBILE_SEND_NEW_MOBILE_CODE("/mobile/sendnewmobilecode", Link.PLATFORM, Type.POST, Certify.YES, "新手机短信验证码"),
	MOBILE_BIND_NEW_CODE("/mobile/bind_new_mobile", Link.PLATFORM, Type.POST, Certify.YES, "验证验证码绑定新手机"),
			
	// 点买接口
	PRODUCT_PRE_BUY_CHECK("/product/pre_buy_check", Link.PLATFORM, Type.POST, Certify.YES, "确认点买前统一校验"),
	PRODUCT_SIMPLE_BUY_CHECK("/product/pre_simple_buy_check", Link.PLATFORM, Type.POST, Certify.YES, "简单的一些点买校验"),
	PRODUCT_PRE_UNION_CHECK("/product/pre_union_check", Link.PLATFORM, Type.POST, Certify.YES, "登陆后统一校验"),

	PRODUCT_BUY_ORDER("/product/buy_order", Link.PLATFORM, Type.POST, Certify.YES, "建仓"),
	PRODUCT_BUY_STATUS("/product/buy_status", Link.PLATFORM, Type.POST, Certify.YES, "查询建仓状态"),
	
	PRODUCT_SELL_ORDER("product/sell_order", Link.PLATFORM, Type.POST, Certify.YES, "平仓"),
	PRODUCT_SELL_STATUS("product/sell_status", Link.PLATFORM, Type.POST, Certify.YES, "查询平仓进度"),
	PRODUCT_PRE_APPLY_SELL_CHECK("product/pre_apply_sell_check", Link.PLATFORM, Type.POST, Certify.YES, "通知平仓前校验"),
	PRODUCT_APPLY_SELL("product/apply_sell", Link.PLATFORM, Type.POST, Certify.YES, "通知平仓"),
	
	PRODUCT_SCHEME_DETAIL("product/scheme_detail", Link.PLATFORM, Type.POST, Certify.NO, "查询方案详情"),
	PRODUCT_PRE_SIMPLE_CHECK("product/pre_simple_check", Link.PLATFORM, Type.POST, Certify.YES, "提前校验接口"),
	PRODUCT_PRE_POLICY_CHECK("product/pre_policy_check", Link.PLATFORM, Type.POST, Certify.YES, "确认买前统一校验"),
	PRODUCT_CREATE_POLICY("product/create_policy", Link.PLATFORM, Type.POST, Certify.YES, "确认发布策略"),
	PRODUCT_POLICY_STATUS("product/policy_status", Link.PLATFORM, Type.POST, Certify.YES, "进度查询"),
	
	AGREEMENT_GET_TIPS("agreement/get_tips", Link.PLATFORM, Type.POST, Certify.YES, "获取重要提示"),
	
	// 查询接口
	PRODUCT_BUYING("product/buying_list",  Link.PLATFORM, Type.POST, Certify.YES, "待建仓列表"),
	PRODUCT_TODO("product/todo_list", Link.PLATFORM, Type.POST, Certify.YES, "查询待通知列表"),
	PRODUCT_SELLING("product/selling_list", Link.PLATFORM, Type.POST, Certify.YES, "待平仓列表"),
	
	PRODUCT_PROFIT("product/profit", Link.PLATFORM, Type.POST, Certify.YES, "查询订单动态信息（状态，浮盈等）"),
	PRODUCT_DETAILS("product/details", Link.PLATFORM, Type.POST, Certify.YES, "交易明细查询"),
	PRODUCT_SETTLEMENT_ORDERS("product/settlements", Link.PLATFORM, Type.POST, Certify.YES, "收付列表"),
	PRODUCT_DO_SETTLE("product/do_settle", Link.PLATFORM, Type.POST, Certify.YES, "进行结算（收付款）"),
	PRODUCT_DEAL_HISTORY("product/deal_history", Link.PLATFORM, Type.POST, Certify.YES, "（收付款）交易流水"),
	
	// 产品其它信息查询及使用  -- 查询接口
	S_DISSENT_COMMIT("/public/dissent_order", Link.PLATFORM, Type.POST, Certify.YES, "创建异议申报"),
	S_DISSENT_ORDERS("/public/user_dissent_orders", Link.PLATFORM, Type.POST, Certify.YES, "用户异议列表"),
	S_USER_COMPENSATE("/public/amendments", Link.PLATFORM, Type.POST, Certify.YES, "修正单列表"),
	S_DISSENT_CORRECTION_PAY("/public/amendments_pay", Link.PLATFORM, Type.POST, Certify.YES, "支付结算修正单"),

	QUALIFICATION_LEVEL("/public/qualification_level", Link.PLATFORM, Type.POST, Certify.YES, "认证等级"),
	CONFIRM_RISK("public/confirm_risk", Link.PLATFORM, Type.POST, Certify.YES, "风险确认"),
	GET_TIME("public/get_time", Link.PLATFORM, "查询服务器时间"),
	
	PRODUCT_POLICY_LIST("/product/policy_list", Link.PLATFORM, Type.POST, Certify.YES, "查询可选策略列表"),
	
	// 数据分享 -- 查询接口
	GET_AD("newestinfo/ad", Link.PLATFORM, "查询广告"),
	
	// 交易行情 -- 查询接口
	GET_HANDICAP("gethq", Link.HQ, "期指盘口"),
	GET_HQ("gethqs", Link.HQ, "行情"),
	GET_MINUTE("get_min", Link.HQ, "期指分时"),
	GET_5K("get_min5k", Link.HQ, "5分钟"),
	GET_15K("get_min15k", Link.HQ, "15分钟"),
	GET_30K("get_min30k", Link.HQ, "30分钟"),
	GET_60K("get_min60k", Link.HQ, "60分钟"),
	GET_K("get_kline", Link.HQ, "期指K线"),
	
	// 其它
	UPDATE_PATH_GET("/update/patchGet/", Link.PLATFORM, "补丁管理更新接口"),
	GET_MY_CUSTOMER_SERVICE("/help/getmycustomerservice", Link.PLATFORM, Type.GET, Certify.YES, "获取专属客服"),
	USER_DEVICE("/device/setuserdevice", Link.PLATFORM, Type.POST, Certify.YES, "设置推送ID"),
	UPDATE_INIPATH("/update/inipatch", Link.PLATFORM, Type.GET, Certify.YES, "获取配置文件"),
	
	
	// h5
	AGREEMENT_DETAIL("agreement/stockcontract", Link.H5, "协议详情"),
	PRODUCT_STRATEGY_AGREEMENT("agreement/getAgreementInfo", Link.H5, "产品协议"),
	PLATFORM_STRATEGY_AGREEMENT("agreement/getServiceAgreementInfo", Link.H5, "平台协议"),
	GET_AGREEMENT_INFO("agreement/getAgreementInfo", Link.H5, "未签署协议内容"),
	LOGIN_CPB("user/login", Link.H5, "操盘宝授权登录"),
	REGISTER_CPB("regsuccess/gounionreg", Link.H5, "操盘宝注册"),
	AUTH_LEVEL("riskcontrol/qualification/index", Link.H5, "认证等级"),
	STRATEGY_AMOUNT("riskcontrol/quota/index", Link.H5, "策略额度"),
	PROD_URL("cpb/gotoCpb", Link.H5, "操盘宝充值、提现、交易流水"),
	LETTER_DETAIL("message/index", Link.H5, "私信详情"),
	SUGGESTION_FEEDBACK("feedback/index", Link.H5, "意见反馈"),
	COMMON_QUESTION("message/commonQuestion", Link.H5, "常见问题"),
	PRIVATE_POLICY("agreement/getPrivacyPolicy", Link.H5, "隐私条款"),
	NEWCOMER_GUIDE("/message/guide", Link.H5, "新手引导");
	
	
	private String mOperationType;
	private Link mLink;
	private Type mRequestType;
	private Certify mNeedCertify;
	private String mName;

	private RequestInfo(String operationType, Link link, String name) {
		this(operationType, link, Type.GET, Certify.NO, name);
	}
	
	private RequestInfo(String operationType, Link link,
			Type requestType, Certify needCertify, String name) {
		this.mOperationType = operationType;
		this.mLink = link;
		this.mRequestType = requestType;
		this.mNeedCertify = needCertify;
		this.mName = name;
	}
	
	public String getOperationType() {
		if (GlobalConstant.isEncrypt) {
			if (this == MOBILE_REGISTER
					|| this == OLD_SET_PWD_NICK
					|| this == OLD_SET_PWD
//					|| this == LOGIN
//					|| this == CHANGE_PWD
					|| this == FORGET_RESET_PWD
					|| this == PWD_CHECK) {
					return mOperationType + "mask";
			}
		}
		return mOperationType;
	}
	
	public String getUrl() {
		if (mLink == Link.PLATFORM) {
			return ServiceConfig.getServicePlatform() + getOperationType();
		} else if (mLink == Link.H5) {
			return ServiceConfig.getServiceH5() + mOperationType;
		} else if (mLink == Link.HQ) {
			return ServiceConfig.getServiceHq();
		} else {
			return mOperationType;
		}
	}

	public String getName() {
		return mName;
	}

	public boolean isPost() {
		return mRequestType == Type.POST;
	}
	
	public boolean needCertify() {
		return mNeedCertify == Certify.YES;
	}
	
}
