package com.gxq.tpm.activity.launch;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.ProductActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.BaseRes.ByteArrayRes;
import com.gxq.tpm.mode.launch.GetVerifyImage;
import com.gxq.tpm.mode.launch.Login;
import com.gxq.tpm.mode.launch.Verify;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.TextWatcherAdapter;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.WebHelper;
import com.gxq.tpm.tools.WebHelper.WebParams;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.CheckEditText;
import com.gxq.tpm.ui.ImageVervifyCheckEditText;
import com.gxq.tpm.ui.PhoneCheckEditText;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.PwdCheckEditText;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ProductLoginActivity extends SuperActivity
		implements OnClickListener, OnEditorActionListener, OnCheckEditTextChangeListener {
	private final static int REQUEST_CODE_LOGIN_BY_SSO = 1001;

	private TextView mTvBottom,  mTvClose,  mTvForgetPwd;
	private PhoneCheckEditText mEtPhone;
	private PwdCheckEditText mEtPassword;
	private ImageVervifyCheckEditText mEtVerity;
	private ImageView mIvVerify;
	private Button mBtnNextStep,mBtnRegisterLgb;
	private View mLoginPhone, mRlVerify, mVLineVerify;

	private int mActivityFrom;
	// 操盘宝登录
	private boolean mLoginByCpb = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_login);

		mActivityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);

		initView();
		initAction();
	}

	private void initView() {
		mBtnNextStep = (Button) findViewById(R.id.btn_next_step);
		mBtnRegisterLgb = (Button) findViewById(R.id.btn_login_register_lgb);

	    mTvClose = (TextView) findViewById(R.id.tv_close);
		mTvBottom = (TextView) findViewById(R.id.tv_bottom);
		mTvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);

		mEtPhone = (PhoneCheckEditText) findViewById(R.id.et_phone);
		mEtPassword = (PwdCheckEditText) findViewById(R.id.et_pwd);
		mEtVerity = (ImageVervifyCheckEditText)findViewById(R.id.et_verify);

		mIvVerify = (ImageView) findViewById(R.id.iv_verify);

		mLoginPhone = findViewById(R.id.rl_login_phone);
		mRlVerify = findViewById(R.id.rl_verify);
		mVLineVerify = findViewById(R.id.view_verify);

		 mTvClose.setText(getString(R.string.login_close));
		checkButtonStatus();
	}

	private void initAction() {
		mBtnNextStep.setOnClickListener(this);
		mBtnRegisterLgb.setOnClickListener(this);

		mTvBottom.setOnClickListener(this);

	    mTvClose.setOnClickListener(this);
	    mTvForgetPwd.setOnClickListener(this);

		mIvVerify.setOnClickListener(this);

		mEtPhone.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				checkVerify();
			}

		});

		mEtPhone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mEtPhone.setOnEditorActionListener(this);
		mEtPhone.setOnCheckEditTextChangeListener(this);
		mEtPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEtPassword.setOnEditorActionListener(this);
		mEtPassword.setOnCheckEditTextChangeListener(this);
		mEtVerity.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEtVerity.setOnEditorActionListener(this);
		mEtVerity.setOnCheckEditTextChangeListener(this);
	}

	private void checkButtonStatus() {
		if (mLoginByCpb) {
			mLoginPhone.setVisibility(View.GONE);
			mTvForgetPwd.setVisibility(View.GONE);

			mBtnNextStep.setText(getString(R.string.login_next_lgb));
			mTvBottom.setText(getString(R.string.login_type_phone));
			mBtnNextStep.setEnabled(true);
			
			mBtnRegisterLgb.setVisibility(View.VISIBLE);
		} else {
			mLoginPhone.setVisibility(View.VISIBLE);
			mTvForgetPwd.setVisibility(View.VISIBLE);

			mTvBottom.setText(getString(R.string.login_type_lgb));
			mBtnNextStep.setText(getString(R.string.login_next_phone));
			mBtnNextStep.setEnabled(checkEditTextStr());
			
			mBtnRegisterLgb.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		checkVerify();
	}

	@Override
	public void onBackPressed() {
		if (mLoginByCpb) {
			// 如果是登录就返回，否则回到大厅
			if (mActivityFrom == ProductIntent.FROM_LOGIN) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			} else if (mActivityFrom == ProductIntent.FROM_SESSION_OUT || mActivityFrom == ProductIntent.FROM_LAUNCH) {
				super.onBackPressed();
			}
		} else {
			mLoginByCpb = true;
			checkButtonStatus();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.et_phone) {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				mEtPassword.requestFocus();
			}
		} else if (v.getId() == R.id.et_password) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				checkLogin();
			} else if (actionId == EditorInfo.IME_ACTION_NEXT) {
				mEtVerity.requestFocus();
			}
		} else if (v.getId() == R.id.et_verify) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				checkLogin();
			}
		}
		return false;
	}

	private void checkLogin() {
		if (mEtPhone.checkText() != CheckEditText.Error.NONE) {
			showToastMsg(mEtPhone.getErrorText());
		} else if (mEtPassword.checkText() != CheckEditText.Error.NONE) {
			showToastMsg(mEtPassword.getErrorText());
		} else if (mRlVerify.getVisibility() == View.VISIBLE && mEtVerity.checkText() != CheckEditText.Error.NONE) {
			showToastMsg(mEtVerity.getErrorText());
		} else if (mBtnNextStep.isEnabled()) {
			// isLogining=true;
			if (mUserPrefs.getIsNeedVerify() && mEtPhone.getPhoneText().equals(mUserPrefs.getLoginInputID())) {
				verifyRequest();
			} else
				loginRequest();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_step:
			if (mLoginByCpb) {
				Intent intent = new Intent(ProductLoginActivity.this, WebActivity.class);
				intent.putExtra(ProductIntent.EXTRA_TITLE, getString(R.string.login_next_lgb));
				intent.putExtra(ProductIntent.EXTRA_URL, 
						WebHelper.addParam(RequestInfo.LOGIN_CPB)
						.addParam(WebParams.CHANNEL, App.instance().getChannel())
						.getUrl());
				intent.putExtra(ProductIntent.EXTRA_FINISH_DIRECT, true);
				intent.putExtra(ProductIntent.EXTRA_CLEAR_CACHE, true);
				startActivityForResult(intent, REQUEST_CODE_LOGIN_BY_SSO);
			} else {
				checkLogin();
			}

			break;
		case R.id.tv_bottom:
			mLoginByCpb = !mLoginByCpb;
			checkButtonStatus();
			break;
		
		case R.id.tv_close: 
			 onBackPressed(); 
			 break;
		 
		case R.id.btn_login_register_lgb:
			Intent intent_login_register_lgb = new Intent(ProductLoginActivity.this, WebActivity.class);
			intent_login_register_lgb.putExtra(ProductIntent.EXTRA_TITLE, getString(R.string.login_register_lgb));
			intent_login_register_lgb.putExtra(ProductIntent.EXTRA_URL,
					WebHelper.addParam(RequestInfo.REGISTER_CPB)
					.addParam(WebParams.CHANNEL, App.instance().getChannel())
					.getUrl());
			intent_login_register_lgb.putExtra(ProductIntent.EXTRA_FINISH_DIRECT, true);
			intent_login_register_lgb.putExtra(ProductIntent.EXTRA_CLEAR_CACHE, true);
			startActivity(intent_login_register_lgb);
			break;
		case R.id.tv_forget_pwd:
			Intent intent_forget_pwd = new Intent(ProductLoginActivity.this, ForgetPwdActivity.class);
			startActivity(intent_forget_pwd);
		case R.id.iv_verify:
			getVerifyImage();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_LOGIN_BY_SSO) {
			if (resultCode == RESULT_OK) {
				loginSuccess();
			}
		}
	}

	private void checkVerify() {
		boolean showVerify = mUserPrefs.getIsNeedVerify()
				&& mEtPhone.getPhoneText().equals(mUserPrefs.getLoginInputID());
		mRlVerify.setVisibility(showVerify ? View.VISIBLE : View.GONE);
		mVLineVerify.setVisibility(showVerify ? View.VISIBLE : View.GONE);
		if (showVerify) {
			getVerifyImage();
		}
	}

	public void getVerifyImage() {
		mEtPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		showWaitDialog(RequestInfo.GET_VERIFY_IMG);
		GetVerifyImage.doRequest(App.instance().dip2px(260 / 2.0f), App.instance().dip2px(88 / 2.0f), this);
	}

	private void verifyRequest() {
		showWaitDialog(RequestInfo.CHECK_VERIFY);
		Verify.Params params = new Verify.Params();
		params.code = mEtVerity.getText().toString();
		params.UUID = UserPrefs.get(ProductLoginActivity.this).getOpenUdid();
		Verify.doRequest(params, ProductLoginActivity.this);
	}

	private void loginRequest() {
		showWaitDialog(RequestInfo.LOGIN);
		Login.Params params = new Login.Params();

		params.login_id = mEtPhone.getPhoneText();
		String pwd = mEtPassword.getText().toString();
		pwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(pwd) : pwd;
		params.password = pwd;

		params.login_type = 1;

		mUserPrefs.setLoginInputID(params.login_id);
		Login.doRequest(params, ProductLoginActivity.this);
	}

	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.LOGIN == info) {
			Login log = (Login) res;
			mUserPrefs.setKEY(log.encryptedKey);
			mUserPrefs.setUid(log.uid);
			mUserPrefs.setIsNeedVerify(false);
			mUserPrefs.setLoginID(mUserPrefs.getLoginInputID());
			mUserPrefs.save();
			App.LockStartTime();

			Util.bindPushService(ProductLoginActivity.this);

			loginSuccess();
		} else if (RequestInfo.CHECK_VERIFY == info) {
			loginRequest();
		} else if (RequestInfo.GET_VERIFY_IMG == info) {
			ByteArrayRes byteArrayRess = (ByteArrayRes) res;
			if (byteArrayRess != null && byteArrayRess.getByteArray() != null) {
				Bitmap bm = Bytes2Bimap(byteArrayRess.getByteArray());
				mIvVerify.setImageBitmap(bm);
			}
		}
	}

	private void loginSuccess() {
		if (mActivityFrom == ProductIntent.FROM_LOGIN) {
			setResult(Activity.RESULT_OK);
		} else {
			Intent intent = new Intent(this, ProductActivity.class);
			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
		finish();
	}

	@Override
	public int netFinishError(RequestInfo info, final int what, String msg, int tag) {
		int type = 0;
		if (RequestInfo.LOGIN == info || RequestInfo.CHECK_VERIFY == info) {
			type = 2;

			new ProductDialog.Builder(this)
//				.setOnCloseListener(null)
				.setContentText(msg, R.drawable.layer_remind)
				.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {

					@Override
					public void onDialogClick(int id) {
						switch (what) {
						case 79002:// 失败次数过多，需要验证码
							mUserPrefs.setIsNeedVerify(true);
							mUserPrefs.save();
							mRlVerify.setVisibility(mUserPrefs.getIsNeedVerify() ? View.VISIBLE : View.GONE);
							mVLineVerify.setVisibility(mUserPrefs.getIsNeedVerify() ? View.VISIBLE : View.GONE);
							getVerifyImage();
							break;
						case 79003:// 失败次数过多，锁定一小时
							break;
						case 79004:// 建议升级
							break;
						case 79005:// 强制升级
							break;

						case 79006:// 验证码错误
						case 79007:// 验证码过期
							getVerifyImage();
							break;
						}
					}
				}).create().show();
		} else if (RequestInfo.GET_VERIFY_IMG == info) {
			networkResultErr(getString(R.string.phone_get_verify_image_fail));
		} else {
			networkResultErr(msg);
			type = 1;
		}
		hideWaitDialog(info);

		return type;
	}

	@Override
	public void onCheckEditTextChanged(Error error) {
		mBtnNextStep.setEnabled(checkEditTextStr());
	}

	private boolean checkEditTextStr() {
		if (mRlVerify.getVisibility() == View.VISIBLE) {
			return ((mEtPhone.checkText() != Error.EMPTY) 
					&& (mEtPassword.checkText() != Error.EMPTY)
					&& (mEtVerity.checkText() != Error.EMPTY));
		} else {
			return ((mEtPhone.checkText() != Error.EMPTY) 
					&& (mEtPassword.checkText() != Error.EMPTY));
		}
	}
}
