package com.gxq.tpm.activity.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.ProductActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.BaseRes.ByteArrayRes;
import com.gxq.tpm.mode.launch.GetVerifyImage;
import com.gxq.tpm.mode.launch.LogOut;
import com.gxq.tpm.mode.launch.Login;
import com.gxq.tpm.mode.launch.Verify;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.GlobalInfo;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.ImageVervifyCheckEditText;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.PwdCheckEditText;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class LockLoginActivity extends SuperActivity
		implements OnEditorActionListener, View.OnClickListener, OnCheckEditTextChangeListener {
	private ImageView mIvHead;
	private TextView mTvPhone;
	private PwdCheckEditText mEtPwd;
	private ImageVervifyCheckEditText mEtVerify;
	private LinearLayout mRlVerify;
	private View mVLineVerify;
	private ImageView mIvVerify;
	private Button mBtnConfirm;

	private Bitmap bm = null;
	private TextView mBtnSwitch;
	private TextView mBtnForget;

	private int mActivityFrom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_login);

		mActivityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);

		initUI();
		bindAction();
	}

	private void initUI() {
		mIvHead = (ImageView) findViewById(R.id.iv_head);
		mTvPhone = (TextView) findViewById(R.id.tv_phone);

		if (mUserPrefs.getUserInfo() != null) {
			GlobalInfo.setNetworkImage(mIvHead, mUserPrefs.getUserInfo().pic, R.drawable.item_head90);
			mTvPhone.setText(mUserPrefs.getUserInfo().bind_mobile);
		}

		mEtPwd = (PwdCheckEditText) findViewById(R.id.et_pwd);
        
		mVLineVerify=findViewById(R.id.view_verify);
		mRlVerify = (LinearLayout) findViewById(R.id.rl_verify);
		mEtVerify = (ImageVervifyCheckEditText)findViewById(R.id.et_verify);
		mIvVerify = (ImageView) findViewById(R.id.iv_verify);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setEnabled(false);

		mBtnSwitch = (TextView) findViewById(R.id.btn_switch);
		mBtnForget = (TextView) findViewById(R.id.tv_forget_password);
	}

	private void bindAction() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);

		mIvVerify.setOnClickListener(this);
		mBtnSwitch.setOnClickListener(this);
		mBtnForget.setOnClickListener(this);

		mEtPwd.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEtPwd.setOnEditorActionListener(this);
		mEtPwd.setOnCheckEditTextChangeListener(this);
		mEtVerify.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEtVerify.setOnEditorActionListener(this);
		mEtVerify.setOnCheckEditTextChangeListener(this);
	}

	private void checkLogin() {
		if (mEtPwd.checkText() != Error.NONE) {
			showToastMsg(mEtPwd.getErrorText());
		} else if (mRlVerify.getVisibility() == View.VISIBLE && mEtVerify.checkText() != Error.NONE) {
			showToastMsg(mEtVerify.getErrorText());
		} else if (mBtnConfirm.isEnabled()) {
			if (mUserPrefs.getIsNeedVerify()) {
				verifyRequest();
			} else {
				loginRequest();
			}
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.et_pwd) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				checkLogin();
			} else if (actionId == EditorInfo.IME_ACTION_NEXT) {
				mEtVerify.requestFocus();
			}
		} else if (v.getId() == R.id.et_verify) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				checkLogin();
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_confirm) {
			checkLogin();
		} else if (v.getId() == R.id.iv_verify) {
			getVerifyImage();
		} else if (v.getId() == R.id.btn_switch) {
			LogOut.doRequest(null, LockLoginActivity.this);
			localLogout();
		} else if (v.getId() == R.id.tv_forget_password) {
			Intent intent = new Intent(LockLoginActivity.this, ForgetPwdActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.iv_back) {
			onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mRlVerify.setVisibility(mUserPrefs.getIsNeedVerify() ? View.VISIBLE : View.GONE);
		mVLineVerify.setVisibility(mUserPrefs.getIsNeedVerify() ? View.VISIBLE : View.GONE);
		if (mUserPrefs.getIsNeedVerify())
			getVerifyImage();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		if (mActivityFrom == ProductIntent.FROM_LOGIN) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			super.onBackPressed();
		}
	}

	private void loginRequest() {
		showWaitDialog(RequestInfo.LOGIN);
		Login.Params params = new Login.Params();
		params.login_id = mUserPrefs.getUid() + "";
		String pwd = mEtPwd.getText().toString();
		pwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(pwd) : pwd;
		params.password = pwd;
		params.login_type = 6;

		Login.doRequest(params, this);
	}

	private void verifyRequest() {
		showWaitDialog(RequestInfo.CHECK_VERIFY);
		Verify.Params params = new Verify.Params();
		params.code = mEtVerify.getText().toString();
		params.UUID = UserPrefs.get(this).getOpenUdid();
		Verify.doRequest(params, this);
	}

	public void getVerifyImage() {
		mEtPwd.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		showWaitDialog(RequestInfo.GET_VERIFY_IMG);
		GetVerifyImage.doRequest(App.instance().dip2px(252 / 2.0f), App.instance().dip2px(108 / 2.0f), this);
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.LOGIN == info) {
			Login log = (Login) res;
			mUserPrefs.setKEY(log.encryptedKey);
			mUserPrefs.setUid(log.uid);
			mUserPrefs.setIsNeedVerify(false);
			mUserPrefs.save();

			App.LockStartTime();

			Util.bindPushService(LockLoginActivity.this);

			if (mActivityFrom == ProductIntent.FROM_LOGIN || mActivityFrom == ProductIntent.FROM_SESSION_OUT) {
				setResult(RESULT_OK);
			} else {
				Intent intent = new Intent(this, ProductActivity.class);
				intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
			finish();
		} else if (RequestInfo.CHECK_VERIFY == info) {
			loginRequest();
		} else if (RequestInfo.GET_VERIFY_IMG == info) {
			hideWaitDialog(info);
			ByteArrayRes byteArrayRes = (ByteArrayRes) res;
			if (byteArrayRes != null && byteArrayRes.getByteArray() != null) {
				bm = Bytes2Bimap(byteArrayRes.getByteArray());
				mIvVerify.setImageBitmap(bm);
			}
		}
	}

	@Override
	public int netFinishError(RequestInfo info, final int what, String msg, int tag) {
		int type = 0;
		if (RequestInfo.LOGIN == info) {
			type = 2;
			new ProductDialog.Builder(this)
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
						}
					}
				}).create().show();
		} else if (RequestInfo.GET_VERIFY_IMG == info) {
			networkResultErr(getString(R.string.phone_get_verify_image_fail));
		} else {
			type = 1;
			networkResultErr(msg);
		}
		hideWaitDialog(info);
		return type;
	}

	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	private void localLogout() {
		GlobalInfo.cleanLogin();
		Intent intent = new Intent(LockLoginActivity.this, ProductLoginActivity.class);
		intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	public void onCheckEditTextChanged(Error error) {
        if(mRlVerify.getVisibility()==View.GONE){
        	mBtnConfirm.setEnabled(mEtPwd.checkText()!=Error.EMPTY);
        }else{
        	mBtnConfirm.setEnabled((mEtVerify.checkText()!=Error.EMPTY)
        			&&(mEtVerify.checkText()!=Error.EMPTY));
        }
	}

}
