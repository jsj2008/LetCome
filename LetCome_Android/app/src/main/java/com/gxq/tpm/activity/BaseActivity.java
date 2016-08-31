package com.gxq.tpm.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.launch.ForgetPwdActivity;
import com.gxq.tpm.activity.launch.ForgetPwdResetActivity;
import com.gxq.tpm.activity.launch.LoadingUPdateActivity;
import com.gxq.tpm.activity.launch.LockLoginActivity;
import com.gxq.tpm.activity.launch.LockPatternActivity;
import com.gxq.tpm.activity.launch.ProductLoginActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkResultInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.SingletonToast;
import com.gxq.tpm.tools.Util;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity implements ICallBack {
	private static final String Tag = "BaseActivity";
	
	public static final int REQUEST_CODE_LOGIN 			= 10001;
	public static final int REQUEST_CODE_SESSION_OUT 	= 10002;
	
	public final static int ERROR_NONE 		= 0;
	public final static int ERROR_TOAST		= 1;
	
	private volatile RequestInfo mInfo;
	
	private LoginCallback mLoginCallback;
	protected boolean mSessionOut;
	protected UserPrefs mUserPrefs = App.getUserPrefs();

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ProductIntent.ACTION_EXIT.equals(action)) {
				finish();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ProductIntent.ACTION_EXIT);
		registerReceiver(mBroadcastReceiver, filter);
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!App.instance().isTest()) {
			MobclickAgent.onPageStart(getClass().getName());
	    	MobclickAgent.onResume(this);
		}

		checkLockScreen();
	}
	
	private void checkLockScreen() {
		if ((this instanceof LaunchActivity)
			|| (this instanceof LaunchActivity) 
			|| (this instanceof ProductLoginActivity)
			|| (this instanceof LockLoginActivity)
			|| (this instanceof ForgetPwdActivity)
			|| (this instanceof ForgetPwdResetActivity)) {
//		 	登录注册流程、解锁页面停止计时
				return;
		}
	    if (mUserPrefs.hasUserLogin()) {
	    	Intent intent = null;
	    	long diffTime = SystemClock.elapsedRealtime()- mUserPrefs.getCurrentTime();
	    	if (mUserPrefs.getLockPrefs().gestureIsOpen()) {
	    		if (diffTime < App.GESTURE_LOCK_TIME && diffTime > 0) {
	    			App.LockStartTime();
	    		} else if (diffTime < App.LOCK_TIME && diffTime >= App.GESTURE_LOCK_TIME) {
	    			intent = new Intent(this, LockPatternActivity.class);
					intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_LOGIN);
	    		} else if (Util.checkUserInfo(mUserPrefs.getUserInfo())) { 
	    			intent = new Intent(this, LockLoginActivity.class);
					intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_SESSION_OUT);
	    		} else {
	    			intent = new Intent(this, ProductLoginActivity.class);
	    			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_SESSION_OUT);
	    		}
	    	} else if (diffTime > App.GESTURE_LOCK_TIME) {
	    		if (Util.checkUserInfo(mUserPrefs.getUserInfo())){
	    			intent = new Intent(this, LockLoginActivity.class);
	    			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_SESSION_OUT);
	    		} else {
	    			intent = new Intent(this, ProductLoginActivity.class);
	    			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_SESSION_OUT);
	    		}
	    	} else {
	    		App.LockStartTime();
	    	}
	    	if (intent != null) {
	    		synchronized (BaseActivity.this) {
	    			// 2秒内弹出过超时的不在提示
	    			if (mSessionOut) return;
	    			mSessionOut = true; 
	    		}
	    		startActivityForResult(intent, REQUEST_CODE_LOGIN);
	    	}
	    }
    }

	@Override
	protected void onPause() {
	    super.onPause();

	    if (!App.instance().isTest()) {
	    	MobclickAgent.onPageEnd(getClass().getName() );
	    	MobclickAgent.onPause(this);
	    }
	    stopLockScreen();
	}

	private void stopLockScreen() {
	    if ((this instanceof LoadingUPdateActivity)
	    		|| (this instanceof LockLoginActivity)
		        || (this instanceof ProductLoginActivity/*LoginActivity*/)
		        || (this instanceof LaunchActivity)
		        || (this instanceof LockPatternActivity)
		        || (this instanceof ForgetPwdActivity)
		        || (this instanceof ForgetPwdResetActivity))
		{// 登录流程、解锁页面停止手势密码
	    	return;
		}
	    if (mUserPrefs.hasUserLogin()) {
	    	App.LockStartTime();
	    }
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
	
	@Override
	public void callBack(RequestInfo info, BaseRes result, int tag) {
		int type = ERROR_NONE;	//0静默形式，1toast形式，2dialog形式
		int errorCode = 0;
		String message = null;

		if (isFinishing()) {
			hideWaitDialog(mInfo);
			return;
		}
		
		try {
			if (result == null) {
				errorCode = NetworkResultInfo.NETWORK_ERROR.getValue();
				message = getString(R.string.net_unusual);
				type = netFinishError(info, errorCode, message, tag);
			} else {
				if (result.error_code == NetworkResultInfo.SESSION_TIMEOUT.getValue()) {
					sessionTimeout(info);
				} else if (result.error_code == NetworkResultInfo.SUCCESS.getValue()) {
					netFinishOk(info, result, tag);
				} else {
					message = result.error_msg;
					if (result != null) {
						netFinishError(info, result, tag);
					}
					type = netFinishError(info, result.error_code, result.error_msg, tag);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Print.i(Tag, e + "");
			errorCode = NetworkResultInfo.SERVICE_ERROR.getValue();
			message = getString(R.string.server_unusual);
			type = netFinishError(info, errorCode, null, tag);
		} finally {
			hideWaitDialog(info);
		}
		
		if (type == ERROR_TOAST) {
			networkResultErr(message);
		}
	}
	
	public void sessionTimeout(RequestInfo info) {
		if (BaseActivity.this.isFinishing()) {
			return;
		}
		
		synchronized (BaseActivity.this) {
			// 2秒内弹出过超时的不在提示
			if (mSessionOut) return;
			mSessionOut = true; 
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (mUserPrefs.hasUserLogin() && Util.checkUserInfo(mUserPrefs.getUserInfo())) {
			Intent intent = new Intent(this, LockLoginActivity.class);
			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_SESSION_OUT);
			startActivityForResult(intent, REQUEST_CODE_SESSION_OUT);
		} else {
			Intent intent = new Intent(this, ProductLoginActivity.class);
			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_SESSION_OUT);
			startActivityForResult(intent, REQUEST_CODE_SESSION_OUT);
		}
	}

	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
	}
	
	public void netFinishError(RequestInfo info, BaseRes result, int tag) {
	}
	
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		int type = 0;
		
		if (null == info || (mInfo != null && mInfo == info)) {
			networkResultErr(msg);
    		type = 1;
		}
		hideWaitDialog(info);
		return type;
	}

	public void networkResultErr(String msg) {
		SingletonToast.getInstance().makeTextWithSpecificGravity(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}
	
	public void showToastMsg(int resId) {
		showToastMsg(getString(resId));
	}
	
	public void showToastMsg(String msg) {
		networkResultErr(msg);
	}

	public void hideWaitDialog(RequestInfo info) {
		if (null == info || (mInfo != null && info == this.mInfo)) {
			Print.w(Tag, "hideWaitDialog," + (info == null ? "" : info.getOperationType()));
			hideWaitDialogD();
		}
	}

	public void showWaitDialog(RequestInfo info) {
		this.mInfo = info;
		Print.w(Tag, "showWaitDialog," + (info == null ? "" : info.getOperationType()));
		
		showWaitDialogD();
	}
	
	protected void hideWaitDialogD() {
	}

	protected void showWaitDialogD() {
	}

	@Override
	public void onBackPressed() {
	    if (this instanceof LockLoginActivity
		        || this instanceof ProductLoginActivity
		        || this instanceof ProductActivity) {
			sendNotify(ProductIntent.ACTION_EXIT);
		} else {
	    	finish();
	    }
	}

	public void sendNotify(String action) {
		// 需要添加跳转页面代码
		Intent intent = new Intent();
		intent.setAction(action);
		sendBroadcast(intent);
	}
	
	public void showLoginActivity(final LoginCallback callback) {
		if (callback == null) return;

		mLoginCallback = callback;
		
		if (mUserPrefs.getUid() > 0 && Util.checkUserInfo(mUserPrefs.getUserInfo())) {
			Intent intent = new Intent(this, LockLoginActivity.class);
			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LOGIN);
			startActivityForResult(intent, REQUEST_CODE_LOGIN);
		} else {
			Intent intent = new Intent(this, ProductLoginActivity.class);
			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LOGIN);
			startActivityForResult(intent, REQUEST_CODE_LOGIN);
		}
	}
	
	public static interface LoginCallback {
		public void login();
		public void cancel();
		public boolean isStrategy(); // 是否是买入过程
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_LOGIN) {
			mSessionOut = false;
			if (resultCode == Activity.RESULT_OK) {
				boolean strategy = false;
				// 回调登录成功操作
				if (mLoginCallback != null) {
					mLoginCallback.login();
					strategy = mLoginCallback.isStrategy();
				}

				if (mUserPrefs.hasUserLogin()) {
					Intent intent = new Intent(this, LoginPrepareActivity.class);
					intent.putExtra(ProductIntent.EXTRA_STRATEGY, strategy);
					startActivity(intent);
				}
			} else {
				if (mLoginCallback != null)
					mLoginCallback.cancel();
				else {
					mUserPrefs.setLogin(false);
					Intent intent = new Intent(this, ProductLoginActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				    startActivity(intent);
				    finish();
				}
			}
			mLoginCallback = null;
		} else if (requestCode == REQUEST_CODE_SESSION_OUT) {
			mSessionOut = false;
			if (resultCode == Activity.RESULT_OK) {
				if (mUserPrefs.hasUserLogin()) {
					Intent intent = new Intent(this, LoginPrepareActivity.class);
					startActivity(intent);
				}
			} else {
				sendNotify(ProductIntent.ACTION_EXIT);
			}
		}
	}
	
}
