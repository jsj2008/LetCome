package com.gxq.tpm.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.mine.CheckSmsVerifyNew;
import com.gxq.tpm.mode.mine.GetSmsVerifyNew;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.CountDown.OnCountDownListener;
import com.gxq.tpm.tools.CountDownUtil;
import com.gxq.tpm.ui.PhoneCheckEditText;
import com.gxq.tpm.ui.PhoneVerifyCheckEditText;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class UserModifyPhoneNewActivity extends SuperActivity 
    implements OnClickListener,OnCheckEditTextChangeListener,OnCountDownListener{
	private final static int SLEEP_SECOND = 60;
	private CountDown mCountTool;
	private TextView mTvGetVerify;
	private PhoneCheckEditText mEtPhoneNumber;
	private PhoneVerifyCheckEditText mEtVerify;
	private Button mBtnNextStep;
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.user_phone_number_title);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_phone_number_new);
		
		mEtPhoneNumber = (PhoneCheckEditText) findViewById(R.id.et_phone);
		mEtPhoneNumber.setOnCheckEditTextChangeListener(this);
		
		mTvGetVerify = (TextView) findViewById(R.id.tv_get_verify);
		mTvGetVerify.setOnClickListener(this);
		
		mEtVerify = (PhoneVerifyCheckEditText) findViewById(R.id.et_verify);
		mEtVerify.setOnCheckEditTextChangeListener(this);
		
		mBtnNextStep = (Button) findViewById(R.id.btn_next_step);
		mBtnNextStep.setText(R.string.btn_confirm);
		mBtnNextStep.setOnClickListener(this);
		mBtnNextStep.setEnabled(false);
		
		mCountTool=new CountDown(SLEEP_SECOND);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CountDownUtil.countStop(mCountTool, mTvGetVerify);
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_get_verify) {
			requestSmsVerify(mEtPhoneNumber.getPhoneText());
		} else if (v.getId() == R.id.btn_next_step) {
			checkSmsVerify(mEtVerify.getText().toString());
		}
	}
	
	private void requestSmsVerify(String phoneNumber) {
		if (mEtPhoneNumber.checkText()!=Error.NONE) {
			showToastMsg(R.string.phone_number_check_error_tips);
		} else {
			showWaitDialog(RequestInfo.MOBILE_SEND_NEW_MOBILE_CODE);
		    GetSmsVerifyNew.Params params = new GetSmsVerifyNew.Params();
		    params.mobile = phoneNumber;
		    GetSmsVerifyNew.doRequest(params, this);
		}
	}
	
	private void checkSmsVerify(String verify) {
		if(mEtPhoneNumber.checkText()!=Error.NONE){
			showToastMsg(mEtPhoneNumber.getErrorText());
		}else if(mEtVerify.checkText()!=Error.NONE){
			showToastMsg(mEtVerify.getErrorText());
		}else{
			showWaitDialog(RequestInfo.MOBILE_BIND_NEW_CODE);
		    CheckSmsVerifyNew.Params params = new CheckSmsVerifyNew.Params();
		    params.mobile = mEtPhoneNumber.getPhoneText();
		    params.mobile_code = verify;
		    CheckSmsVerifyNew.doRequest(params, this);
		}
	}
	private String setBind_mobile(String phonenum){
		return phonenum.substring(0, 3)+"****"+phonenum.substring(7, 11);
	}
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (info == RequestInfo.MOBILE_SEND_NEW_MOBILE_CODE) {
			GetSmsVerifyNew verify = (GetSmsVerifyNew) res;
			if (BaseRes.RESULT_OK.equals(verify.result)) {
				CountDownUtil.countStart(mCountTool, this, 1);
			}
		} else if (info == RequestInfo.MOBILE_BIND_NEW_CODE) {
			CheckSmsVerifyNew verify = (CheckSmsVerifyNew) res;
			if (BaseRes.RESULT_OK.equals(verify.result)) {
				String phonenum=mEtPhoneNumber.getPhoneText();
				mUserPrefs.setLoginID(phonenum);
				mUserPrefs.setLoginInputID(phonenum);
				UserInfo userInfo=mUserPrefs.getUserInfo();
			    userInfo.bind_mobile=setBind_mobile(phonenum);
			    mUserPrefs.setUserInfo(userInfo);
				new ProductDialog.Builder(this)
					.setContentText(R.string.phone_modify_success_tips, R.drawable.layer_success)
					.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						@Override
						public void onDialogClick(int id) {
							finish(); 
							Intent intentbroadcast = new Intent();  
							intentbroadcast.setAction(ProductIntent.ACTION_FINSH_MODIFY_PHONEACT);  					  
					        sendBroadcast(intentbroadcast);     
						}
					}).create().show();
			} else {
				new ProductDialog.Builder(this)
					.setContentText(R.string.phone_number_verify_check_error_tips, R.drawable.layer_remind)
					.setPositiveButton(R.string.btn_confirm, null)
					.create().show();
			}
		}
	}
	
	@Override
	public void onCheckEditTextChanged(Error error) {
		mBtnNextStep.setEnabled(mEtPhoneNumber.checkText()!=Error.EMPTY
				&&mEtVerify.checkText()!=Error.EMPTY);
	}

	@Override
	public void onCountDownFinished() {
		CountDownUtil.countStop(mCountTool, mTvGetVerify);
	}

	@Override
	public void onCountDownRun(int maxNum, int remainNum) {
		CountDownUtil.setCountTextRun(remainNum, mTvGetVerify);
		
	}
	
}
