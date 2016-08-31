package com.gxq.tpm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.Nickname;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.Nickname.Params;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.ui.NickNameCheckEditText;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class SetNickNameActivity extends SuperActivity implements OnCheckEditTextChangeListener {
	private NickNameCheckEditText mEtNickname;
	private TextView mTvErrorNotice;
	private Button mTvEnsure;

	private String mNickName;
	private boolean mReset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nickname);
		
		mReset = getIntent().getBooleanExtra(ProductIntent.EXTRA_RESET_NICK_NAME, false);
		initUI();
		bindAction();
		
		setFinishOnTouchOutside(false);		
	}

//	@Override
//	protected void initBar() {
//		super.initBar();
//		mReset = getIntent().getBooleanExtra(ProductIntent.EXTRA_RESET_NICK_NAME, false);
//		
//		if (mReset) {
//			getTitleBar().setTitle(R.string.reset_nick_name_title);
//		} else {
//			getTitleBar().setTitle(R.string.set_nick_name_title);
//		}
//	}

	@Override
	public void onLeftClick(View v) {
	}

	@Override
	public void onBackPressed() {
	}

	private void initUI() {
		mEtNickname = (NickNameCheckEditText) findViewById(R.id.et_nickname);
		mTvEnsure = (Button) findViewById(R.id.btn_confirm);
		mTvEnsure.setEnabled(false);
		
		mTvErrorNotice = (TextView) findViewById(R.id.tv_error_notice);
		
		if (mReset) {
			((TextView)findViewById(R.id.tv_nickname_title)).setText(R.string.reset_nick_name_title);
			mTvErrorNotice.setVisibility(View.VISIBLE);
			mTvErrorNotice.setText(R.string.nickname_descript_reset);
		}
	}

	private void bindAction() {
		mEtNickname.setOnCheckEditTextChangeListener(this);
		mTvEnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mEtNickname.checkText()!=Error.NONE){
					showToastMsg(mEtNickname.getErrorText());
				}else{
					mNickName = mEtNickname.getText().toString().trim();

					Nickname.Params params = new Params();
					params.nickname = mNickName;

					showWaitDialog(RequestInfo.SET_NICKNAME);
					Nickname.doRequest(params, SetNickNameActivity.this);
				}
			}
		});
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (RequestInfo.SET_NICKNAME == info) {
			Nickname nn = (Nickname) res;
			if (BaseRes.RESULT_OK.equals(nn.result)) {
				UserInfo userInfo = mUserPrefs.getUserInfo();
				userInfo.nick_name = mNickName;
				mUserPrefs.setUserInfo(userInfo);
				mUserPrefs.save();
				
				setResult(Activity.RESULT_OK);
				finish();
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.SET_NICKNAME == info) {
			return ERROR_TOAST;
		}
		return super.netFinishError(info, what, msg, tag);
	}

	@Override
	public void onCheckEditTextChanged(Error error) {
		mTvEnsure.setEnabled(mEtNickname.checkText()!=Error.EMPTY);
	}
}
