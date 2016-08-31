package com.gxq.tpm.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.letcome.App;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.NetworkResultInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.DispatcherTimer.OnDispatcherTimerListener;
import com.gxq.tpm.ui.CTitleBar;
import com.umeng.analytics.MobclickAgent;

public class FragmentBase extends Fragment implements ICallBack, OnDispatcherTimerListener {
	private int mMarkId;
//	private OnSwitchListener mSwitchListener;
	protected SuperActivity mContext;
	protected boolean mViewCreated;
	
	public interface OnSwitchListener {
		public void onSwitch(int markId, Bundle data);
	}
	
	public FragmentBase(){
		super();
	}
	
	public FragmentBase(int markId) {
		super();
		mMarkId = markId;
	}

	public int getMarkId() {
		return mMarkId;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mContext = (SuperActivity) getActivity();
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mViewCreated = true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (isHidden())
			return;
		
		if (!App.instance().isTest()) {
			MobclickAgent.onPageStart(this.getClass().getName());
		}
		getView().setClickable(true);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    if (!App.instance().isTest()) {
	    	MobclickAgent.onPageEnd(this.getClass().getName());
	    }
	}
	
//	public void setSwitchListener(OnSwitchListener l) {
//		mSwitchListener = l;
//	}
//	
//	public OnSwitchListener getSwitchListener() {
//		return mSwitchListener;
//	}
	
	public void resetFocus() {
	}
	
//	/**
//	 * 
//	 *  @Description    : 方法描述  特殊返回键处理
//	 *  @return         : boolean
//	 *  @Creation Date  : 2014-11-3 下午1:33:30 
//	 *  @Author         : morong
//	 *  @Update Date    :
//	 *  @Update Author  : morong
//	 */
//	@Override
//	public boolean keyboardVisible(){
//		return false;
//	}

	@Override
	public void callBack(RequestInfo info, BaseRes result, int tag) {
		int type = -1;	//0静默形式，1toast形式，2dialog形式
		int errorCode = 0;
//		String errorMessage = null;
		String message = null;
		if (getActivity() == null || getActivity().isFinishing() || isHidden() || isDetached())
			return;
		try {
			if (result == null) {
				errorCode = NetworkResultInfo.NETWORK_ERROR.getValue();
				message = getString(R.string.net_unusual);
				type = netFinishError(info, errorCode, message, tag);
			} else {
				if (result.error_code == NetworkResultInfo.SESSION_TIMEOUT.getValue()) {
					mContext.sessionTimeout(info);
				} else if (result.error_code == NetworkResultInfo.SUCCESS.getValue()) {
//					if (RequestInfo.PROTOCOL_UNSIGNED == info) {
//						UnsignAgreement unsign = (UnsignAgreement) result;
//						ArrayList<UnsignAgreement.Agreement> mUnsign = unsign.res_data;
//						if (null != mUnsign && mUnsign.size() > 0) {
//							Intent intent = new Intent(getActivity(), SignAgreementActivity.class);
//							intent.putExtra(ProductIntent.EXTRA_UNSIGN_AGREEMENT, mUnsign);
//							startActivity(intent);
//						}
//					}
					netFinishOk(info, result, tag);
				} else {
					type = netFinishError(info, result.error_code, result.error_msg, tag);
				}
			}
		} catch (Exception e) {
			Print.i(this.getClass().getName(), e + "");
			errorCode = NetworkResultInfo.SERVICE_ERROR.getValue();
			message = getString(R.string.server_unusual);
			type = netFinishError(info, errorCode, null, tag);
		} finally {
			hideWaitDialog(info);
		}
		
		if (type == 1) {
			
		}
		
//		try {
//			BaseRes res;
//			if (object instanceof String) {
//				String content = (String) object;
//				if (content != null && content.startsWith("\\ufeff")) {
//					content = content.substring(1);
//				}
//				
//				Gson gson = new Gson();
//				res = gson.fromJson(content, BaseRes.class);
//				errorCode = String.valueOf(res.error_code);
//				errorMessage = res.error_msg;
//				message = errorMessage;
//				
//				/*if (res.error_code == NetworkResultInfo.WITH_UNSIGNED_AGREEMENT.getValue() ||
//						res.error_code == NetworkResultInfo.WITH_UNSIGNED_FRAME_AGREEMENT.getValue()){
//					String prd_type = res.product_type;
//					String user_type = App.prefs.getUserInfo().user_type + "";
//					UnsignAgreement.doRequest(prd_type, user_type, this, FragmentBase.class.getName());
//				} else */if (res.error_code == NetworkResultInfo.SESSION_TIMEOUT
//				        .getValue()) {
//					Log.d("FragmentBase", "operationType = " + operationType);
//					((BaseActivity)getActivity()).sessionTimeout(null, res.error_msg);
//				} else if (res.error_code == NetworkResultInfo.SUCCESS.getValue()) {
//					netFinishOk(operationType, (String) object, tag);
//				} else {
//					type = error(operationType, res.error_code, res.error_msg, tag);
//				}
//			} else if (state == NetworkConstants.STATUS_NETWORK_OK) {
//				res = (BaseRes) object;
//				errorCode = String.valueOf(res.error_code);
//				errorMessage = res.error_msg;
//				message = errorMessage;
//				
//				/*if (res.error_code == NetworkResultInfo.WITH_UNSIGNED_AGREEMENT.getValue() ||
//						res.error_code == NetworkResultInfo.WITH_UNSIGNED_FRAME_AGREEMENT.getValue()){
//					String prd_type = res.product_type;
//					String user_type = App.prefs.getUserInfo().user_type+"";
//					UnsignAgreement.doRequest(prd_type, user_type, this , FragmentBase.class.getName());
//				} else */if (res.error_code == NetworkResultInfo.SESSION_TIMEOUT
//				        .getValue()) {
//					//((BaseActivity)getActivity()).hideWaitDialog();
////					hideWaitDialog(operationType);
//					((BaseActivity)getActivity()).sessionTimeout(null, res.error_msg);
//				} else {
//					if (res.error_code == NetworkResultInfo.SUCCESS.getValue()) {
//						if (null != tag && tag.equals(FragmentBase.class.getName()) && RequestInfo.PROTOCOL_UNSIGNED.getOperationType().equals(operationType)){
//							UnsignAgreement unsign = (UnsignAgreement) res;
//							ArrayList<Agreement> mUnsign = unsign.res_data;
//							if (null != mUnsign && mUnsign.size() > 0) {
//								Intent intent = new Intent(getActivity(), SignAgreementActivity.class);
//								intent.putExtra(ProductIntent.EXTRA_UNSIGN_AGREEMENT, mUnsign);
//								startActivity(intent);
//							}
//						}
//						netFinishOk(operationType, res, tag);
//					} else {
//						type = error(operationType, res.error_code, res.error_msg, tag);
//					}
//				}
//			} else if (state == NetworkConstants.STATUS_NETWORK_UNAVAILABLE) {
//				errorCode = String.valueOf(NetworkConstants.STATUS_NETWORK_UNAVAILABLE);
//				message = getString(R.string.net_unusual);
//				type = error(operationType, NetworkConstants.STATUS_NETWORK_UNAVAILABLE, null, tag);
//			} else {
//				errorCode = String.valueOf(NetworkConstants.STATUS_SERVICE_UNAVAILABLE);
//				message = getString(R.string.server_unusual);
//				type = error(operationType, NetworkConstants.STATUS_SERVICE_UNAVAILABLE, null, tag);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Print.i(this.getClass().getName(), e + "");
//			errorCode = String.valueOf(NetworkConstants.STATUS_SERVICE_UNAVAILABLE);
//			message = getString(R.string.server_unusual);
//			type = error(operationType, NetworkConstants.STATUS_SERVICE_UNAVAILABLE, null, tag);
//		} finally {
//			//((BaseActivity)getActivity()).hideWaitDialog();
//			hideWaitDialog(operationType);
//		}
//		if (type != -1) {
//			mContext.saveErrorMessage(type, operationType, errorCode, errorMessage, message);
//		}

	}

	public int netFinishError(RequestInfo info, int what, String msg, int tag){
		return mContext.netFinishError(info, what, msg, tag);
	}	

	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
	}
	
	public void networkResultErr(String msg) {
		mContext.networkResultErr(msg);
	}

	public void showWaitDialog(RequestInfo info) {
		mContext.showWaitDialog(info);
	}
	
	public void hideWaitDialog(RequestInfo info) {
		mContext.hideWaitDialog(info);
	}
	
	public CTitleBar getTitleBar() {
		return mContext.getTitleBar();
	}
	
	protected DispatcherTimer initNewTimer(int interval) {
		return initNewTimer(interval, 0);
	}
	
	protected DispatcherTimer initNewTimer(int interval, int operationType) {
		return new DispatcherTimer(this, interval, operationType);
	}
	
	@Override
	public void onAlarmClock(int operationType) {
	}
	
}
