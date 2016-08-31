package com.gxq.tpm.activity;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.cooperation.HomeCompleteUserInfoActivity;
import com.gxq.tpm.activity.launch.LockPatternActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.ProductPreCheck;
import com.gxq.tpm.mode.UnsignAgreement;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.cooperation.ConfirmRisk;
import com.gxq.tpm.mode.launch.PrepareMsg;
import com.gxq.tpm.mode.launch.ProductPreUnionCheck;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.ProductDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class LoginPrepareActivity extends SuperActivity {
	private final static int REQUEST_CODE_AGREEMENT		= 1001;
	private final static int REQUESR_CODE_SET_NICKNAME	= 1002;
	private final static int REQUESR_CODE_SET_GESTURE	= 1003;
	private final static int REQUEST_COMPLETE_USERINFO	= 1004;
	
	private boolean mBindMobile;
	private boolean mStrategy;
	
	private ProductDialog.ProductDialogListener mAuthFinishListener = new ProductDialog.ProductDialogListener() {
		public void onDialogClick(int id) {
			requestPopupMsg();
		}
	};
	
//	private Dialog.OnDismissListener mPopupMsgDissmissListener = new Dialog.OnDismissListener() {
//		@Override
//		public void onDismiss(DialogInterface dialog) {
//			finish();
//		}
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login_prepare);
		setFinishOnTouchOutside(false);
		
		if (getIntent() != null) 
			mStrategy = getIntent().getBooleanExtra(ProductIntent.EXTRA_STRATEGY, false);
		
		PrepareMsg.doRequest(null);
		
		// 1.协议, 2.昵称, 3.认证(点买校验)
		// 1.协议, 2.完善个人资料, 3.手势密码
		requestPlatformUnsignAgreement();
	}
	
	private void requestPlatformUnsignAgreement() {
		showWaitDialog(RequestInfo.PROTOCOL_UNSIGNED);
		
		UnsignAgreement.Params params = new UnsignAgreement.Params();
		params.prd_type = GlobalConstant.PRODUCT_TYPE_PLATFORM;
		params.user_type = GlobalConstant.USER_TYPE_STRATEGY;
		UnsignAgreement.doRequest(params, this);
	}
	
	private void requestUserInfo() {
		showWaitDialog(RequestInfo.USER_INFO);
		
		UserInfo.Params params = new UserInfo.Params();
		UserInfo.doRequest(params, this);
	}
	
	private void requestPreCheck() {
		showWaitDialog(RequestInfo.PRODUCT_PRE_UNION_CHECK);
		
		ProductPreUnionCheck.doRequest(this);
	}
	
	private void requestPopupMsg() {
		finish();
//		showWaitDialog(RequestInfo.MSG_POPUP_MESSAGE);
//		PopupMsg.doRequest(this);
	}
	
	@Override
	public void finish() {
		super.finish();
		
		overridePendingTransition(0, R.anim.anim_disappear);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.PROTOCOL_UNSIGNED == info) {
			UnsignAgreement unsign = (UnsignAgreement) res;
			if (unsign.res_data != null && unsign.res_data.size() > 0) {
				showSingleAgreement(unsign.res_data.get(0));
			} else {
				requestUserInfo();
			}
		} else if (RequestInfo.USER_INFO == info) {
			UserInfo userInfo = (UserInfo) res;
			mUserPrefs.setUserInfo(userInfo);
			
			if (null == userInfo.bind_mobile || userInfo.bind_mobile.equals("")) {
				Intent intent = new Intent(this, HomeCompleteUserInfoActivity.class);
				startActivityForResult(intent, REQUEST_COMPLETE_USERINFO);
				mBindMobile = true;
			} else if (TextUtils.isEmpty(userInfo.nick_name)) {
				showNickName(userInfo.nicknm_status, REQUESR_CODE_SET_NICKNAME);
			} else if (mBindMobile) {
				if (!App.getUserPrefs().getLockPrefs().gestureIsSetted()) {
					Intent intent = new Intent(this, LockPatternActivity.class);
					intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_SET);
					startActivityForResult(intent, REQUESR_CODE_SET_GESTURE);
				} else {
					requestPreCheck();
				}
			} else {
				requestPreCheck();
			}
		} else if (info == RequestInfo.PRODUCT_PRE_UNION_CHECK) {
			final ProductPreUnionCheck buyCheck = (ProductPreUnionCheck) res;
			if (BaseRes.RESULT_OK.equals(buyCheck.result)) {
				requestPopupMsg();
			} else {
				if (buyCheck.reason == ProductPreCheck.AUTH) {
					showAuthDialog(buyCheck);
				} else if (buyCheck.reason == ProductPreCheck.RISK_NOTICE) {
					if (mStrategy) {
						showRiskNoticeDialog(buyCheck);
					} else {
						requestPopupMsg();
					}
				} else if (buyCheck.reason == ProductPreCheck.RISK_INFORM) {
					if (mStrategy) {
						if (mUserPrefs.showAuthInform()) {
							showAuthInfromDialog(buyCheck);
						} else {
							requestPopupMsg();
						}
					} else {
						requestPopupMsg();
					}
				} else {
					requestPopupMsg();
				}
			}
		} else if (info == RequestInfo.CONFIRM_RISK) {
			requestPopupMsg();
//		} else if (info == RequestInfo.MSG_POPUP_MESSAGE) {
//			PopupMsg msg = (PopupMsg) res;
//			if (msg.popup == 1){
//				Util.showMsgDialg(msg, this).setOnDismissListener(mPopupMsgDissmissListener);
//				readMsg(msg);
//			} else if (msg.popup == 2){
//				Util.showMsgWindow(msg, this).setOnDismissListener(mPopupMsgDissmissListener);
//				readMsg(msg);
//			} else {
//				finish();
//			}
//			finish();
		}
	}
	
	private void showAuthDialog(ProductPreUnionCheck buyCheck) {
		Util.showAuthDialog(this, buyCheck, mAuthFinishListener,
				new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						Util.gotoAuth(LoginPrepareActivity.this);
						finish();
					}
				});
	}

	private void showRiskNoticeDialog(final ProductPreUnionCheck buyCheck) {
		Util.showRiskNoticeDialog(this, buyCheck, mAuthFinishListener,
				new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						showWaitDialog(RequestInfo.CONFIRM_RISK);
						ConfirmRisk.Params params = new ConfirmRisk.Params();
						params.msg_id = buyCheck.msg_id;
						ConfirmRisk.doRequest(params, LoginPrepareActivity.this);
					}
				});
	}

	private void showAuthInfromDialog(ProductPreUnionCheck buyCheck) {
		Util.showAuthInfromDialog(this, buyCheck, new ProductDialog.ProductDialogListener() {
			@Override
			public void onDialogClick(int id) {
				mUserPrefs.saveAuthInform();
				requestPopupMsg();
			}
		}, mAuthFinishListener);
		
	}

//	private void readMsg(PopupMsg msg){
//		ReadMsg.Params params = new ReadMsg.Params();
//		params.msg_id = msg.id;
//		ReadMsg.doRequest(params, this);
//	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.PROTOCOL_UNSIGNED == info) {
			requestUserInfo();
		} else if (RequestInfo.USER_INFO == info) {
			requestUserInfo();
		} else if (RequestInfo.PRODUCT_PRE_UNION_CHECK == info) {
			requestPopupMsg();
		} else if (info == RequestInfo.CONFIRM_RISK) {
			requestPopupMsg();
		} else if (info == RequestInfo.MSG_POPUP_MESSAGE) {
			finish();
		}
			
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void showSingleAgreement(UnsignAgreement.Agreement agreement) {
		Intent intent = new Intent(this, SignAgreementActivity.class);
		intent.putExtra(ProductIntent.EXTRA_UNSIGN_AGREEMENT, agreement);
		intent.putExtra(ProductIntent.EXTRA_TYPE, ProductIntent.UNSIGN_AGREEMENT_SIGN);
		
		startActivityForResult(intent, REQUEST_CODE_AGREEMENT);
	}
	
	private void showNickName(int status, int requestCode) {
		Intent intent = new Intent(this, SetNickNameActivity.class);
		intent.putExtra(ProductIntent.EXTRA_RESET_NICK_NAME, status == 2);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_AGREEMENT) {
			requestUserInfo();
		} else if (requestCode == REQUESR_CODE_SET_NICKNAME) {
			if (resultCode == Activity.RESULT_OK) {
				requestPreCheck();
			} else {
				sendNotify(ProductIntent.ACTION_EXIT);
			}
		} else if (requestCode == REQUESR_CODE_SET_GESTURE) {
			requestPreCheck();
		} else if (requestCode == REQUEST_COMPLETE_USERINFO) {
			if (resultCode == Activity.RESULT_OK) {
				requestUserInfo();
			} 
		}
	}
	
	@Override
	public void onBackPressed() {
	}
	
}
