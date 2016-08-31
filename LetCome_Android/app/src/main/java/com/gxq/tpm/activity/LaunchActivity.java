package com.gxq.tpm.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.StockService;
import com.gxq.tpm.activity.launch.LoadingUPdateActivity;
import com.gxq.tpm.activity.launch.LockPatternActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UpdatePathGet;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.FileDownload;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.ProductDialog;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class LaunchActivity extends SuperActivity {
	private final static int REQUEST_CODE_GESTURE = 1;
	
	private final static int LAUNCH_WAIT_TIME = 3000;
	
	private Handler mHandler = new Handler();
	
	private ProductDialog mUpdateDialog;
	private TextView mTvLoading;
	private String mWaitText = "";
	private Runnable mLoadingRunnable = new Runnable() {
		@Override
		public void run() {
			waitForDownLoad(mTvLoading);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		if (!App.instance().isTest()) {
			//zxc umeng start
			MobclickAgent.setDebugMode(true);
//      	SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//			然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
			MobclickAgent.openActivityDurationTrack(false);
//			MobclickAgent.setAutoLocation(true);
//			MobclickAgent.setSessionContinueMillis(1000);
			/** 设置是否对日志信息进行加密, 默认false(不加密). */
			AnalyticsConfig.enableEncrypt(true);
			MobclickAgent.updateOnlineConfig(this);
			//end
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Intent service = new Intent(this, StockService.class);
		startService(service);
	    
	    mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				requestVersionChecking();
			}
		}, LAUNCH_WAIT_TIME);
	}
	
	private void requestVersionChecking() {
		UpdatePathGet.Params params = new UpdatePathGet.Params();
		params.name = "version_android";
		UpdatePathGet.doRequest(params, this);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		
		if (info == RequestInfo.UPDATE_PATH_GET) {
			final UpdatePathGet update = (UpdatePathGet) res;
			String currentVersion = App.instance().getVersionName();
			
			if (update.res_data != null && needUpdate(update.res_data.version, currentVersion)
					&& !mUserPrefs.getLastIgnoredVersion().equalsIgnoreCase(update.res_data.version)) {
				showUpdateDialog(update);
			} else {
				launch();
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (info == RequestInfo.UPDATE_PATH_GET) {
			launch();
		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	private boolean needUpdate(String version, String currentVersion) {
		String[] oldVersions = currentVersion.split("\\.");
		String[] newVersions = version.split("\\.");
		
		int length = Math.min(oldVersions.length, newVersions.length);
		
		for (int i = 0; i < length; i++) {
			int siVersion = Integer.parseInt(oldVersions[i]);
			int ciVersion = Integer.parseInt(newVersions[i]);
			if (siVersion > ciVersion) return false;
			if (siVersion < ciVersion) return true;
		}
		
		return oldVersions.length < newVersions.length;
	}
	
	private void showUpdateDialog(final UpdatePathGet update) {
		if (update.res_data.flag == 1) {
			mUpdateDialog = new ProductDialog.Builder(this)
				.setContent(R.string.force_update_title, R.string.force_update_content)
				.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						showDownloadDialig(update.res_data.url);
					}
				})
			.create();
			mUpdateDialog.show();
		} else {
			mUpdateDialog = new ProductDialog.Builder(this)
				.setContent(R.string.update_title, R.string.update_content)
				.setPositiveButton(R.string.update_later, new ProductDialog.ProductDialogListener(){
					@Override
					public void onDialogClick(int id) {
						mUserPrefs.setLastIgnoredVersion(update.res_data.version);
						launch();
					}
				})
				.setNegativeButton(R.string.update_now, new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						showDownloadDialig(update.res_data.url);
					}
				})
			.create();
			mUpdateDialog.show();
		}
	}
	
	private void showDownloadDialig(String url) {
		if (url == null) {
			launch();
			return;
		}
		mUpdateDialog = new ProductDialog.Builder(this)
			.setContentView(R.layout.dialog_loading)
			.create();
		final TextView tvPercent = (TextView) mUpdateDialog.findViewById(R.id.tv_percent);
		tvPercent.setText(0 + Util.transformString(R.string.percent_unit));
		final ProgressBar pbPercent = (ProgressBar) mUpdateDialog.findViewById(R.id.pb_percent);
		mTvLoading = (TextView) mUpdateDialog.findViewById(R.id.tv_loading);
		
		mUpdateDialog.show();
		
		FileDownload.startDownLoadApk(url, new FileDownload.DownloadListener() {
			@Override
			public void percent(int percent) {
				if (percent > 100) {
					percent = 100;
				}
				tvPercent.setText(percent + Util.transformString(R.string.percent_unit));
				pbPercent.setProgress(percent);
			}
			
			@Override
			public void failed() {
				mHandler.removeCallbacks(mLoadingRunnable);
				mUpdateDialog.dismiss();
				requestVersionChecking();
			}
			
			@Override
			public void done(final String filePath) {
				mHandler.removeCallbacks(mLoadingRunnable);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						openFile(filePath);
					}
				});
				
			}
		});
		mHandler.postDelayed(mLoadingRunnable, 500);
	}
	
	private void waitForDownLoad(final TextView loading) {
		if (mWaitText.length() < 6) {
			mWaitText += ".";
		} else {
			mWaitText = ".";
		}
		loading.setText(mWaitText);
		mHandler.postDelayed(mLoadingRunnable, 500);
	}
	
	private void openFile(String filePath) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.parse("file://"+filePath), type);
		startActivity(intent);
	}
	
	private void launch() {
		mUserPrefs.setLogin(false);
		
		Intent intent = new Intent();
		intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
		
		if (!mUserPrefs.getLastInstructedVersion().equals(App.instance().getVersionName())) {
			intent.setClass(this, LoadingUPdateActivity.class);
		} else if (mUserPrefs.getUid() > 0 &&
				mUserPrefs.getLockPrefs().gestureIsOpen()) { // 手势密码是否设置并打开
			intent.setClass(this, LockPatternActivity.class);
			intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_LOGIN);
			startActivityForResult(intent, REQUEST_CODE_GESTURE);
			return;
		} else {
			intent.setClass(LaunchActivity.this, ProductActivity.class);
		}
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE_GESTURE == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				mUserPrefs.setLogin(true);
				
				Intent intent = new Intent(this, ProductActivity.class);
				intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
				startActivity(intent);
			}
			finish();
		}
	}
}
