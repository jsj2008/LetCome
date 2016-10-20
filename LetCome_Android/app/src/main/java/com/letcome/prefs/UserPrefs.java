package com.letcome.prefs;

import android.content.Context;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.gxq.tpm.prefs.BasePrefs;
import com.gxq.tpm.tools.Util;
import com.letcome.mode.CategoriesRes;
import com.letcome.mode.LoginRes;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class UserPrefs extends BasePrefs {
	private static UserPrefs userPrefs;
	private static final String PREFS_NAME 				= "UserPrefs";
	private static final String OPEN_UDID 				= "open_udid";//设备唯一标示
	private static final String SESSION 				= "session_id";//网络请求session
	private static final String UID 					= "uid";//用户Id
	private static final String QQ 					= "QQ";//用户Id
	private static final String RID 					= "rid";//本地request编号
	private static final String KEY 					= "key_crypt";//本地request编号
	private static final String IS_NEED_VERIFY 			= "is_need_verify";//是否需要验证码
	private static final String LOGIN_ID 				= "phone"; //手机号码
	private static final String LOGIN_INPUT_ID 			= "last_phone";//上次登陆输入的手机号码
	private static final String USER_INFO 				= "user_info";//用户账号信息
	private static final String LAST_IGNORED_VERSION 	= "last_ignored_version"; // 忽略版本
	private static final String LAST_INSTRUCTED_VERSION = "last_instructed_version"; // 欢迎页
	private static final String CURRENT_TIME 			= "current_time"; //
	private static final String CATEGORIES 				= "CATEGORIES"; //

	private static final String AUTH_INFORM				= "auth_inform"; // 认证告知
	
	private static final int ONE_WEEK					= 7 * 24 * 60 * 60 * 1000; // 一个星期
	
	public static String MINE_MSG 			= "mine_msg";
	public static String PRODUCT_MSG 		= "product_msg";
	public static String ANNO_MSG 			= "anno_msg"; // 公告未读消息数量
	public static String NOTICE_MSG 		= "notice_msg"; // 通知未读消息数量
	public static String SYSTEM_MSG 		= "system_msg"; // 系统未读消息数量
	
//	private boolean mLogin;

	
	private UserPrefs(Context context) {
		super(context, PREFS_NAME);
	}

	public static UserPrefs get(Context context) {
		if (userPrefs == null) {
			userPrefs = new UserPrefs(context);
		}
		return userPrefs;
	}



	public void setOpenUdid(String v) {
		putString(OPEN_UDID, v);
	}

	public String getOpenUdid() {
		return getString(OPEN_UDID, "");
	}
	
	public void setSession(String v) {
		putString(SESSION, v);
	}

	public String getSession() {
		return getString(SESSION, "");
	}

	public void setQq(String v) {
		putString(QQ, v);
	}

	public String getQq() {
        return getString(QQ, "");
	}

	public void setUid(String v) {
		putString(UID, v);
	}

	public String getUid() {
		return getString(UID, "");
	}

	public void logout(){
		setSession(null);
		setUid(null);
		setLoginID(null);
		setUserInfo(null);
		setIsNeedVerify(false);
		save();
	}

//	public void setLogin(boolean login) {
//		mLogin = login;
//	}
//
//	public boolean hasUserLogin() {
//		return mLogin && getUid() > 0;
//	}

	public boolean hasUserLogin() {
		String session = getSession();
		return getSession()!=null && session.length() > 0;
	}

    public boolean hasQQ() {
        String qq = getQq();
        return qq !=null && qq.length() > 0;
    }
	
	public boolean hasPhoneLogin() {
		return Util.checkMoblie(getLoginID());
	}
	
	public void setRid(long v) {
		putLong(RID, v);
	}

	public long getRid() {
		return getLong(RID, 0);
	}	
	
	public String getLoginInputID() {
		return getString(LOGIN_INPUT_ID, EMPTY_STRING);
	}

	public void setLoginInputID(String v) {
		putString(LOGIN_INPUT_ID, v);
		save();
	}
	
	public String getLoginID() {
		return getString(LOGIN_ID, "");
	}

	public void setLoginID(String v) {
		putString(LOGIN_ID, v);
	}

	/**
	 * zxc 保存签名用的key,进行加密。
	 */
	public void setKEY(String v) {
		String add="%3D";
		String str=add+v+add;
		String newStr="";
        try {
	        newStr = new String(str.getBytes("UTF-8"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
        }
		putString(KEY, newStr);
    }
	
	public String getKEY() {
		String add="%3D";
		String str="";
		try {
			str = new String(getString(KEY, "").getBytes("ISO-8859-1"),"UTF-8");
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
		if (str.length() == 0) return "";
		
		str=str.substring(0,str.lastIndexOf(add));
		str=str.substring(add.length());
	    return str;
    }
	
	public void setIsNeedVerify(Boolean v) {
		putBoolean(IS_NEED_VERIFY, v);
	}

	public Boolean getIsNeedVerify() {
		return getBoolean(IS_NEED_VERIFY, false);
	}	

	public void setUserInfo(LoginRes info) {
		writeBaseRes(USER_INFO, info);
	}

	public LoginRes getUserInfo() {
		return readBaseRes(USER_INFO, LoginRes.class);
	}

	public void setCategories(CategoriesRes categories) {
		writeBaseRes(CATEGORIES, categories);
	}

	public CategoriesRes getCategories() {
		return readBaseRes(CATEGORIES, CategoriesRes.class);
	}

	public String getFlag(){
		return getString("flag", "");
	}
	public void setFlag(String flag){
		putString("flag", flag);
	}

	public void setLastIgnoredVersion(String v) {
		 putString(LAST_IGNORED_VERSION, v);
		 save();
	}

	public String getLastIgnoredVersion() {
		return getString(LAST_IGNORED_VERSION, EMPTY_STRING);
	}
	
	public void setLastInstructedVersion(String v) {
		 putString(LAST_INSTRUCTED_VERSION, v);
		 save();
	}

	public String getLastInstructedVersion() {
		return getString(LAST_INSTRUCTED_VERSION, EMPTY_STRING);
	}
	
	synchronized public void setCurrentTime(long time) {
		putLong(CURRENT_TIME, time);
		save();
	}
	
	synchronized public long getCurrentTime() {
		return getLong(CURRENT_TIME, SystemClock.elapsedRealtime());
	}

	

	
	public boolean showAuthInform() {
		long authInform = getLong(AUTH_INFORM, 0);
		if (authInform == 0 || 
				(authInform > 0 && System.currentTimeMillis() - authInform > ONE_WEEK)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void saveAuthInform() {
		putLong(AUTH_INFORM, System.currentTimeMillis());
		save();
	}

	
	public long getMineMsgTime() {
		return getLong(MINE_MSG, 0);
	}
	
	public void setMineMsgTime(long time) {
		putLong(MINE_MSG, time);
		save();
	}
	
	public long getProductMsgTime() {
		return getLong(PRODUCT_MSG, 0);
	}
	
	public void setProductMsgTime(long time) {
		putLong(PRODUCT_MSG, time);
		save();
	}
	
	public long getAnnoMsgTime() {
		return getLong(ANNO_MSG, 0);
	}
	
	public void setAnnoMsgTime(long time) {
		putLong(ANNO_MSG, time);
		save();
	}
	
	public long getNoticeMsgTime() {
		return getLong(NOTICE_MSG, 0);
	}
	
	public void setNoticeMsgTime(long time) {
		putLong(NOTICE_MSG, time);
		save();
	}
	
	public long getSystemMsgTime() {
		return getLong(SYSTEM_MSG, 0);
	}
	
	public void setSystemMsgTime(long time) {
		putLong(SYSTEM_MSG, time);
		save();
	}
	
	private void writeBaseRes(String key, Serializable res) {
		if (res == null) {
			putString(key, EMPTY_STRING);
		} else {
			Gson gson = new Gson();
			putString(key, gson.toJson(res));
		}
	}
	
	private <T> T readBaseRes(String key, Class<T> cls) {
		String value = getString(key, null);
		if (value == null || value.length() == 0) return null;
		Gson gson = new Gson();
		return gson.fromJson(value, cls);
	}
	
}
