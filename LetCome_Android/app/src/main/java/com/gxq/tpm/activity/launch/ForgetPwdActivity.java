package com.gxq.tpm.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.launch.ForgetGetMoblieCode;
import com.gxq.tpm.mode.launch.ForgetMoblieCheck;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.CountDownUtil;
import com.gxq.tpm.ui.CheckEditText;
import com.gxq.tpm.ui.PhoneCheckEditText;
import com.gxq.tpm.ui.PhoneVerifyCheckEditText;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class ForgetPwdActivity extends SuperActivity implements View.OnClickListener,
		OnEditorActionListener,OnCheckEditTextChangeListener {
//	private static final String Tag = "ForgetPwdActivity";
	private static final int SECOND_COUNT_MAX = 60;
	
//	private UserPrefs prefs;
	private PhoneCheckEditText mEtPhone;
	private PhoneVerifyCheckEditText mEtVerify;
	private TextView mTvGetVerify;
	private Button mBtnCommit;
	
	private CountDown  mCountTool;
	
//	protected int codeWrongCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		mCountTool =new CountDown(SECOND_COUNT_MAX);
		initUI();
		bindAction();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	};
	
	@Override
	public void onPause() {
		super.onPause();
		
	}
	
	@Override
	protected void onDestroy() {
		CountDownUtil.countStop(mCountTool, mTvGetVerify);
		super.onDestroy();
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.login_forget_pwd_title);
		
		getTitleBar().showBackImage();
	}

	private void initUI() {
		mEtPhone = (PhoneCheckEditText) findViewById(R.id.et_phone);
		
		mEtVerify = (PhoneVerifyCheckEditText) findViewById(R.id.et_verify);
		mTvGetVerify = (TextView) findViewById(R.id.tv_verify);
		
		mBtnCommit = (Button)findViewById(R.id.bt_commit);
    }

	private CountDown.OnCountDownListener mCountListener = new CountDown.OnCountDownListener() {
	
		@Override
		public void onCountDownRun(int maxNum, int remainNum) {
			CountDownUtil.setCountTextRun(remainNum, mTvGetVerify);
		}
	
		@Override
		public void onCountDownFinished() {
			CountDownUtil.countStop(mCountTool, mTvGetVerify);
		}
	};
	
	private void bindAction() {
		mTvGetVerify.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);
		
		mEtPhone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mEtPhone.setOnEditorActionListener(this);
		mEtPhone.setOnCheckEditTextChangeListener(this);
		mEtVerify.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEtVerify.setOnEditorActionListener(this);
		mEtVerify.setOnCheckEditTextChangeListener(this);
    }
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.et_phone) {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				mEtVerify.requestFocus();
			}
		}/* else if (v.getId() == R.id.et_img_verify) {
			if (actionId == EditorInfo.IME_ACTION_DONE && mBtnCommit.isEnabled()) {
				getMoblieCode();
			}
		}*/
		return false;
	}
	

	@Override
	public void onLeftClick(View v) {
		super.onLeftClick(v);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
    public void onClick(View v) {
	    switch (v.getId()) {
		case R.id.tv_verify:
			if (mEtPhone.checkText() == Error.NONE) {
				getMoblieCode();
			} else {
				showToastMsg(mEtPhone.getErrorText());
			}
			break;	
		case R.id.bt_commit:
			if (mEtPhone.checkText() != CheckEditText.Error.NONE){
				showToastMsg(mEtPhone.getErrorText());
			} else if (mEtVerify.checkText() != CheckEditText.Error.NONE){
				showToastMsg(mEtVerify.getErrorText());
			} else {
				if (mBtnCommit.isEnabled()) {
					mobileCheckRequest();
				}
			}
			break;
		default:
			break;
		}
    }

	private void getMoblieCode() {
        ForgetGetMoblieCode.Params params = new ForgetGetMoblieCode.Params();
		params.mobile = mEtPhone.getPhoneText();
//		params.UUID = UserPrefs.get(this).getOpenUdid();
		ForgetGetMoblieCode.doRequest(params, ForgetPwdActivity.this);
//		showWaitDialog(RequestInfo.FORGET_MOBILE_CODE_GET);
    }
	
	private void mobileCheckRequest(){
		ForgetMoblieCheck.Params params=new ForgetMoblieCheck.Params();
		params.mobile = mEtPhone.getPhoneText();
		params.code	= mEtVerify.getText().toString();
		ForgetMoblieCheck.doRequest(params, this);
		showWaitDialog(RequestInfo.FORGET_MOBILE_CHECK);
    }
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
	    super.netFinishOk(info, res, tag);
//	    if(RequestInfo.CHECK_VERIFY.getOperationType().equals(operationType)){
//	    	mobileCheckRequest();
//	    }
	    if (RequestInfo.FORGET_MOBILE_CODE_GET == info){
	    	ForgetGetMoblieCode code = (ForgetGetMoblieCode)res;
	    	if(BaseRes.RESULT_OK.equals(code.result)){
				if(mTvGetVerify.isEnabled()){
					CountDownUtil.countStart(mCountTool, mCountListener, 1);
				}
	    	}
	    	
//	    	if(mr.result.equals("Y")) {
//	    		Intent intent = new Intent(ForgetPwdActivity.this, ForgetPwd2Activity.class);
//				intent.putExtra(ProductIntent.EXTRA_MOBILE, mEtPhone.mgetText());
//				startActivity(intent);
//				finish();
////	    		countStart();
//	    	}
	    } else if (RequestInfo.FORGET_MOBILE_CHECK == info){
	    	ForgetMoblieCheck moblieCheck = (ForgetMoblieCheck) res;
	    	if(BaseRes.RESULT_OK.equals(moblieCheck.result)){
	    		Intent intent = new Intent(ForgetPwdActivity.this, ForgetPwdResetActivity.class);
				intent.putExtra(ProductIntent.EXTRA_MOBILE, mEtPhone.getPhoneText());
				startActivity(intent);
				finish();
	    	}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		int type = 0;
		if (RequestInfo.FORGET_MOBILE_CHECK == info) {
			fail(msg);
			type = 2;
		} else {
			networkResultErr(msg);
			type = 1;
		}
		hideWaitDialog(info);
		return type;
	}
	
	private void fail(String msg) {
		new ProductDialog.Builder(this)
			.setContentText(msg, R.drawable.layer_remind)
			.setPositiveButton(R.string.btn_confirm, null)
			.create().show();
    }

	@Override
	public void onCheckEditTextChanged(Error error) {
		mBtnCommit.setEnabled((mEtPhone.checkText()!=Error.EMPTY)
				&&(mEtVerify.checkText()!=Error.EMPTY));
	}
}
