package com.letcome;

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
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.gxq.tpm.tools.BaiduLocationUtils;
import com.gxq.tpm.tools.Installation;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.crypt.MD5;
import com.letcome.prefs.UserPrefs;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.List;

public class App extends Application {

	private final static String CHANNEL				= "UMENG_CHANNEL";
	
	private static App mInstance;
	private static UserPrefs prefs;
	
	private DisplayMetrics mDisplayMetrics;
	
	private String mVersion;
	private Bundle mPackageInfo;
	private String mChannel;

	
	public static App instance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		prefs = UserPrefs.get(mInstance);
//		dbManager = DBManager.getInstance(mInstance);
//		dbManager.init();
//
		mDisplayMetrics = getResources().getDisplayMetrics();
		
		Util.initialize(this);
		initData();
	}
	
	private void initData() {

		prefs.setOpenUdid(MD5.md5(getDeviceId() + Installation.id(this)));
		prefs.setFlag("ok");
		prefs.save();
        File cachecheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

		SDKInitializer.initialize(getApplicationContext());
		BaiduLocationUtils.createtLocationAndCity(this);
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
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int sp2px(float spValue) {
		final float fontScale = getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
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
//		if (mChannel == null) {
//			mChannel = App.instance().getAppInfoBundle().getString(CHANNEL);
//			if (Util.isEmpty(mChannel)) {
//				mChannel = "" + App.instance().getAppInfoBundle().getInt(CHANNEL);
//			}
//		}
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
	
	public boolean isTest() {
		return BuildConfig.IS_TEST;
	}

	public static UserPrefs getUserPrefs() {
		return prefs;
	}


}
