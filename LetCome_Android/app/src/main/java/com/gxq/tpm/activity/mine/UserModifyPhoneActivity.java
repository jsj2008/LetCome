package com.gxq.tpm.activity.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.gxq.tpm.mode.mine.CheckSmsVerify;
import com.gxq.tpm.mode.mine.GetSmsVerify;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CountDown.OnCountDownListener;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.CountDownUtil;
import com.gxq.tpm.ui.PhoneVerifyCheckEditText;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class UserModifyPhoneActivity extends SuperActivity 
    implements OnClickListener,OnCheckEditTextChangeListener ,OnCountDownListener{
	private final static int SLEEP_SECOND = 60;
	private CountDown mCountTool;
	private TextView mTvGetVerify;
	private PhoneVerifyCheckEditText mEtVerify;
	private Button btnNextStep;
	
	private BroadcastReceiver mFinishMySelfBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ProductIntent.ACTION_FINSH_MODIFY_PHONEACT.equals(action)) {
				finish();
			}
		}
	};
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.user_phone_number_title);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ProductIntent.ACTION_FINSH_MODIFY_PHONEACT);
		registerReceiver(mFinishMySelfBroadcastReceiver, filter);
		
		setContentView(R.layout.activity_phone_number);
		
		UserInfo userInfo = App.getUserPrefs().getUserInfo();
		if (userInfo != null && userInfo.bind_mobile != null) {
			((TextView) findViewById(R.id.tv_phone_number)).setText(userInfo.bind_mobile);
		}
		
		mTvGetVerify = (TextView) findViewById(R.id.tv_get_verify);
		mTvGetVerify.setOnClickListener(this);
		
		mEtVerify = (PhoneVerifyCheckEditText) findViewById(R.id.et_verify);
		mEtVerify.setOnCheckEditTextChangeListener(this);
		btnNextStep = (Button) findViewById(R.id.btn_next_step);
		
		btnNextStep.setText(R.string.btn_next_step);
		btnNextStep.setOnClickListener(this);
		btnNextStep.setEnabled(false);
		
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
		unregisterReceiver(mFinishMySelfBroadcastReceiver);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_get_verify) {
			requestSmsVerify();
		} else if (v.getId() == R.id.btn_next_step) {
			checkSmsVerify(mEtVerify.getText().toString());
		}
	}
	
	private void requestSmsVerify() {
		showWaitDialog(RequestInfo.MOBILE_SEND_CODE_FOR_AUTH);
		GetSmsVerify.doRequest(this);
	}
	
	private void checkSmsVerify(String verify) {
		if (mEtVerify.checkText() != Error.NONE) {
			showToastMsg(mEtVerify.getErrorText());
		} else{
			showWaitDialog(RequestInfo.MOBILE_CHECK_MOBILE_CODE);
		    CheckSmsVerify.Params params = new CheckSmsVerify.Params();
		    params.mobile_code = verify;
		    CheckSmsVerify.doRequest(params, this);
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (info == RequestInfo.MOBILE_SEND_CODE_FOR_AUTH) {
			GetSmsVerify verify = (GetSmsVerify) res;
			if (BaseRes.RESULT_OK.equals(verify.result)) {
				CountDownUtil.countStart(mCountTool, this, 1);
			}
		} else if (info == RequestInfo.MOBILE_CHECK_MOBILE_CODE) {
			CheckSmsVerify verify = (CheckSmsVerify) res;
			if (BaseRes.RESULT_OK.equals(verify.result)) {
				gotoNextStep();
			} else {
				new ProductDialog.Builder(this)
					.setContentText(R.string.phone_number_verify_check_error_tips, R.drawable.layer_remind)
					.setPositiveButton(R.string.btn_confirm, null)
					.create().show();
			}
		}
	}
	
	private void gotoNextStep() {
		Intent intent = new Intent(this, UserModifyPhoneNewActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCheckEditTextChanged(Error error) {
		btnNextStep.setEnabled(mEtVerify.checkText()!=Error.EMPTY);
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
