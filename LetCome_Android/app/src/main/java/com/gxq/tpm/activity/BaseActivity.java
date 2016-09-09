package com.gxq.tpm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.NetworkResultInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.SingletonToast;
import com.letcome.R;

public class BaseActivity extends FragmentActivity implements ICallBack {
	private static final String Tag = "BaseActivity";
	
	public static final int REQUEST_CODE_LOGIN 			= 10001;
	public static final int REQUEST_CODE_SESSION_OUT 	= 10002;
	
	public final static int ERROR_NONE 		= 0;
	public final static int ERROR_TOAST		= 1;
	
	private volatile RequestInfo mInfo;

	protected boolean mSessionOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	protected void onResume() {
		super.onResume();
	}
	


	@Override
	protected void onPause() {
	    super.onPause();
	}


	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
				if (result.error_code == NetworkResultInfo.SUCCESS.getValue() || result.result != BaseRes.RESULT_FAIL ) {
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


	public void sendNotify(String action) {
		// 需要添加跳转页面代码
		Intent intent = new Intent();
		intent.setAction(action);
		sendBroadcast(intent);
	}

	
}
