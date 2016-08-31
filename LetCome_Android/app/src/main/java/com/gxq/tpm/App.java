package com.gxq.tpm;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

//import com.letcome.R;
import com.gxq.tpm.db.DBManager;
import com.gxq.tpm.mode.SearchStock;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.Installation;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.crypt.MD5;

public class App extends Application {
	public static final int GESTURE_LOCK_TIME       = 5 * 60 * 1000;// 手势超时锁屏时间
	public static final int LOCK_TIME      			= 15 * 60 * 1000;// 超时锁屏时间
	private final static String CHANNEL				= "UMENG_CHANNEL";
	
	private static App mInstance;
	private static UserPrefs prefs;
	private static DBManager dbManager;
	
	private DisplayMetrics mDisplayMetrics;
	
	private String mVersion;
	private Bundle mPackageInfo;
	private String mChannel;
	
	private SearchStock mSearchStock;
	
	public static App instance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		prefs = UserPrefs.get(mInstance);
		dbManager = DBManager.getInstance(mInstance);
		dbManager.init();
		
		mDisplayMetrics = getResources().getDisplayMetrics();
		
		Util.initialize(this);
		initData();
	}
	
	private void initData() {
		prefs.setOpenUdid(MD5.md5(getDeviceId()+Installation.id(this)));
		prefs.setFlag("ok");
		prefs.save();
		
	}
	
	/**
	 * @return 设备号
	 */
	private String getDeviceId() {
		String deviceId = null;
		TelephonyManager tm = (TelephonyManager) getApplicationContext()
		        .getSystemService(Context.TELEPHONY_SERVICE);
		if (null != tm) {
			deviceId = tm.getDeviceId();
			if (null == deviceId)
				deviceId = "";
		}
		return deviceId;
	}


	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(float dpValue) {
		final float scale = mDisplayMetrics.density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int sp2px(float spValue) {
		final float fontScale = getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 *
	 * @Description : 十分钟内无操作，发送手势锁屏消息
	 * @return : void
	 * @Creation Date : 2014-6-25 下午5:21:06
	 * @Author : HuNan
	 * @Update Date :
	 * @Update Author : HuNan
	 */
	public static void LockStartTime() {
		prefs.setCurrentTime(SystemClock.elapsedRealtime());
	}
	
//	/**
//	 *
//	 * @Description : 停止锁屏消息计时
//	 * @return : void
//	 */
//	public void LockStopTime() {
//		prefs.setIsShowLock(false);
//		prefs.save();
//	}

	/**
	 *
	 * @Description : 判断程序是否在前台
	 * @return : boolean 在前台返回true，在后台返回false
	 * @Creation Date : 2014-6-30 上午11:00:37
	 * @Author : HuNan
	 * @Update Date :
	 * @Update Author : HuNan
	 */
	public boolean appIsShow() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = "com.gxq.qfgj";
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			ComponentName topConponent = tasksInfo.get(0).topActivity;
			if (packageName.equals(topConponent.getPackageName())) {
				return true;
			} else {
				// 当前的APP在后台运行
				return false;
			}
		}
		return false;
	}

	public Bundle getAppInfoBundle() {
		if (mPackageInfo == null) {
			try {
				ApplicationInfo appInfo = App.instance().getPackageManager()
				        .getApplicationInfo(App.instance().getPackageName(),
				                PackageManager.GET_META_DATA);
				if (appInfo != null) {
					mPackageInfo = appInfo.metaData;
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}	
		return mPackageInfo;
	}
	
	public String getChannel() {
		if (mChannel == null) {
			mChannel = App.instance().getAppInfoBundle().getString(CHANNEL);
			if (Util.isEmpty(mChannel)) {
				mChannel = "" + App.instance().getAppInfoBundle().getInt(CHANNEL);
			}
		}
		return mChannel;
	}
	
	/**
	 * 版本号
	 */
	public String getVersionName() {
		if (mVersion == null) {
			PackageInfo pinfo = null;
	        try {
	        	pinfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
	        } catch (NameNotFoundException e) {
		        e.printStackTrace();
	        }
			
			mVersion = (pinfo == null || pinfo.versionName == null) ? "" : pinfo.versionName;
		}
		return mVersion;
	}
	
//	public boolean isTest() {
//		return getResources().getBoolean(R.bool.isTest);
//	}
	
//	public static boolean isLogin() {
//		return prefs.getUid() > 0;
//	}
	
	public SearchStock getSearchStock() {
		if (mSearchStock == null) {
			mSearchStock = prefs.getSearchStock();
			if (mSearchStock == null) {
				mSearchStock = new SearchStock();
			}
		}
		return mSearchStock;
	}
	
	public void saveSearchStock() {
		prefs.setSearchStock(mSearchStock);
		prefs.save();
	}
	
	public static UserPrefs getUserPrefs() {
		return prefs;
	}
	
	public static DBManager getDBManager() {
		return dbManager;
	}
	
}
