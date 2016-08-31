package com.gxq.tpm.activity.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.launch.ProductLoginActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.Nickname;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.Nickname.Params;
import com.gxq.tpm.mode.cooperation.SetMobileAndPassword;
import com.gxq.tpm.mode.mine.GetSmsVerifyNew;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.CountDownUtil;
import com.gxq.tpm.tools.GlobalInfo;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.CheckEditText;
import com.gxq.tpm.ui.PhoneCheckEditText;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;
import com.gxq.tpm.ui.ProductDialog.ProductDialogListener;

public class HomeCompleteUserInfoActivity extends SuperActivity implements
		OnClickListener {

	private static final int SECOND_COUNT_MAX = 60;

	private PhoneCheckEditText mEtPhone;
	private CheckEditText mEtNickName, mEtVerify, mEtPassword;
	private TextView mTvGetVerify;
	private ImageView mIvPasswordDisplay;

	private View mViewNickName, mViewPhone;

	private Button mBtnCommit;

	private CountDown mCountTool;

	private boolean isPasswordHidden = true;
	
	private String mNickName;
	
	private OnCheckEditTextChangeListener mListener = new OnCheckEditTextChangeListener() {
		@Override
		public void onCheckEditTextChanged(Error error) {
			checkCommitStatus();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_user_info);
		mCountTool = new CountDown(SECOND_COUNT_MAX);
		initView();
		initAction();
	}

	@Override
	protected void initBar() {
		super.initBar();

		getTitleBar().setTitle(R.string.complete_user_info_title);
	}

	private void initView() {
		mEtNickName = (CheckEditText) findViewById(R.id.et_nickname);
		mEtPhone = (PhoneCheckEditText) findViewById(R.id.et_phone);
		mEtVerify = (CheckEditText) findViewById(R.id.et_verify);
		mEtPassword = (CheckEditText) findViewById(R.id.et_password);

		mTvGetVerify = (TextView) findViewById(R.id.tv_verify);

		mIvPasswordDisplay = (ImageView) findViewById(R.id.iv_password_dispaly);

		mBtnCommit = (Button) findViewById(R.id.btn_commit);
		
		mViewNickName = findViewById(R.id.ll_nickname);
		mViewPhone = findViewById(R.id.ll_phone_and_password);

		showPhoneView();
	}

	private void showPhoneView() {
		mViewNickName.setVisibility(View.GONE);
		mViewPhone.setVisibility(View.VISIBLE);

		mBtnCommit.setText(R.string.btn_next_step);
		mBtnCommit.setEnabled(false);
		getTitleBar().setTitle(getString(R.string.complete_user_info_title_phone));
	}

	private void initAction() {
		mTvGetVerify.setOnClickListener(this);
		mIvPasswordDisplay.setOnClickListener(this);
		mBtnCommit.setOnClickListener(this);

		mEtNickName.setOnCheckEditTextChangeListener(mListener);
		mEtPhone.setOnCheckEditTextChangeListener(mListener);
		mEtVerify.setOnCheckEditTextChangeListener(mListener);
		mEtPassword.setOnCheckEditTextChangeListener(mListener);
	}
	
	private void checkCommitStatus() {
		if (mViewNickName.isShown()) {
			mBtnCommit.setEnabled(mEtNickName.checkText() != Error.EMPTY);
		} else {
			mBtnCommit.setEnabled(mEtPhone.checkText() != Error.EMPTY
					&& mEtVerify.checkText() != Error.EMPTY
					&& mEtPassword.checkText() != Error.EMPTY);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_verify:
			if (mEtPhone.checkText() != Error.NONE) {
				showToastMsg(mEtPhone.getErrorText());
			} else if (mTvGetVerify.isEnabled()) {
				getMoblieCode();
			}
			break;

		case R.id.iv_password_dispaly:
			if (isPasswordHidden) {
				mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				isPasswordHidden = false;
				mIvPasswordDisplay.setBackgroundResource(R.drawable.item_display);
			} else {
				// 设置EditText文本为隐藏的
				mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				isPasswordHidden = true;
				mIvPasswordDisplay.setBackgroundResource(R.drawable.item_hide);
			}
			CharSequence charSequence = mEtPassword.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}
			break;
		case R.id.btn_commit:
			if (mViewNickName.isShown()) {
				checkLogin();
			} else {
				checkPhoneAndPassword();
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
//		if (mViewNickName.isShown()) {
//			showPhoneView();
//		}else{
		if (mViewPhone.isShown()) {
			new ProductDialog.Builder(this)/*.setOnCloseListener(null)*/
			.setContentText(R.string.complete_user_info_exit)
			.setNegativeButton(R.string.btn_cancel, null)
			.setPositiveButton(R.string.btn_confirm, new ProductDialogListener() {
				
				@Override
				public void onDialogClick(int id) {
					setResult(RESULT_CANCELED);
					localLogout();
				}
			}).create().show();
		}
	}

	private void getMoblieCode() {
		showWaitDialog(RequestInfo.MOBILE_SEND_NEW_MOBILE_CODE);

		GetSmsVerifyNew.Params params = new GetSmsVerifyNew.Params();
		params.mobile = mEtPhone.getPhoneText();
		GetSmsVerifyNew.doRequest(params, HomeCompleteUserInfoActivity.this);
	}

	private void setMobileAndPassword() {
		showWaitDialog(RequestInfo.SET_MOBILE_AND_PASSWORD);
		SetMobileAndPassword.Params params = new SetMobileAndPassword.Params();
		params.mobile = mEtPhone.getPhoneText();
		params.mobile_code = mEtVerify.getText().toString();
		String pwd = mEtPassword.getText().toString();
		params.password = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(pwd) : pwd;
		SetMobileAndPassword.doRequest(params, this);
	}
	
	private void setNickName(){
		mNickName = mEtNickName.getText().toString().trim();
		
		showWaitDialog(RequestInfo.SET_NICKNAME);
		Nickname.Params params = new Params();
		params.nickname = mNickName;		
		Nickname.doRequest(params, this);
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.MOBILE_SEND_NEW_MOBILE_CODE == info) {
			GetSmsVerifyNew getSmsVerifyNew = (GetSmsVerifyNew) res;
			if (BaseRes.RESULT_OK.equals(getSmsVerifyNew.result)) {
				CountDownUtil.countStart(mCountTool, mCountListener, 1);
			}
		} else if (RequestInfo.SET_MOBILE_AND_PASSWORD == info) {
			hideWaitDialog(info);
			SetMobileAndPassword password = (SetMobileAndPassword) res;
			if (null != password && BaseRes.RESULT_OK.equals(password.result)) {
				mUserPrefs.setLoginID(mEtPhone.getPhoneText());
				mUserPrefs.save();
				showNickNameView();
			}
		} else if (RequestInfo.SET_NICKNAME == info) {
			hideWaitDialog(info);
			Nickname nn = (Nickname) res;
			if (BaseRes.RESULT_OK.equals(nn.result)) {
				UserInfo userInfo = mUserPrefs.getUserInfo();
				userInfo.nick_name = mNickName;
				mUserPrefs.setUserInfo(userInfo);
				mUserPrefs.save();
				
				showToastMsg(R.string.complete_user_info_success);
				
				setResult(RESULT_OK);
				finish();
			}
		}
	}
	
	private void showNickNameView() {
		mViewNickName.setVisibility(View.VISIBLE);
		mViewPhone.setVisibility(View.GONE);

		mBtnCommit.setText(R.string.complete_user_info_save);
		mBtnCommit.setEnabled(false);
		
		getTitleBar().setTitle(getString(R.string.set_nick_name_title));
		
		CountDownUtil.countStop(mCountTool, mTvGetVerify);
	}

	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		hideWaitDialog(info);
//		showPhoneView();
//		clearInput();
		return super.netFinishError(info, what, msg, tag);
	}

	private CountDown.OnCountDownListener mCountListener = new CountDown.OnCountDownListener() {

		@Override
		public void onCountDownRun(int maxNum, int remainNum) {
			CountDownUtil.setCountTextRun(remainNum, mTvGetVerify);
		}

		@Override
		public void onCountDownFinished() {
			CountDownUtil.countStop(mCountTool, mTvGetVerify,mEtPhone);
		}
	};

	protected void setCountText(int remainNum) {
		if (remainNum == 0) {
			mTvGetVerify.setText(R.string.phone_get_number_verify);
			mTvGetVerify.setEnabled(true);
			mTvGetVerify.setBackgroundResource(R.drawable.btn_white_selector);

		} else {
			mTvGetVerify.setEnabled(false);
			mTvGetVerify.setText(getString(R.string.phone_verify_countdown, remainNum));
			mTvGetVerify.setBackgroundColor(getResources().getColor(R.color.color_e0e0e0));
		}
	}

	private void checkPhoneAndPassword() {
		if (mEtPassword.checkText() != Error.NONE) {
			showToastMsg(mEtPhone.getErrorText());
		} else if (mEtVerify.checkText() != Error.NONE) {
			showToastMsg(mEtVerify.getErrorText());
		} else if (mEtPassword.checkText() != Error.NONE) {
			showToastMsg(mEtPassword.getErrorText());
		} else {
			setMobileAndPassword();
		}
	}

	private void checkLogin() {
		if (mEtNickName.checkText() != Error.NONE) {
			showToastMsg(mEtNickName.getErrorText());
		} else {
			setNickName();
		}
	}
	
	private void localLogout() {
	    GlobalInfo.cleanLogin();
	    Intent intent = new Intent(HomeCompleteUserInfoActivity.this, ProductLoginActivity.class);
	    intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    startActivity(intent);
	    finish();
    }

}
