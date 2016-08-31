package com.gxq.tpm.activity.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.ProductActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.launch.Login;
import com.gxq.tpm.mode.launch.ResetPwd;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.PwdCheckEditText;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class ForgetPwdResetActivity extends SuperActivity implements View.OnClickListener,
		OnEditorActionListener,OnCheckEditTextChangeListener{
	private UserPrefs mPrefs;
	private PwdCheckEditText mEtPassword1;
	private PwdCheckEditText mEtPassword2;
	private Button mBtCommit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd_reset);
				
		initUI();
		bindAction();
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.login_reset_pwd_title);
		getTitleBar().showBackImage();
	}

	private void initUI() {
		mEtPassword1 = (PwdCheckEditText) findViewById(R.id.et_pwd1);
		mEtPassword2 = (PwdCheckEditText) findViewById(R.id.et_pwd2);
		
//		mEtPassword1.setTransformationMethod(new AsteriskPasswordTransformationMethod());  
//		mEtPassword2.setTransformationMethod(new AsteriskPasswordTransformationMethod());  

		mBtCommit = (Button)findViewById(R.id.btn_commit);
    }
	
	private void bindAction() {
		mBtCommit.setEnabled(false);
	    
		mBtCommit.setOnClickListener(this);
		
		mEtPassword1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		mEtPassword1.setOnEditorActionListener(this);
		mEtPassword1.setOnCheckEditTextChangeListener(this);
		mEtPassword2.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mEtPassword2.setOnEditorActionListener(this);
		mEtPassword2.setOnCheckEditTextChangeListener(this);
    }
	
	private boolean checkCommit() {
		String strNew = mEtPassword1.getText().toString();
		String strRepeat = mEtPassword2.getText().toString();
		return strNew.equals(strRepeat);
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.et_pwd1) {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				mEtPassword2.requestFocus();
			}
		} else if (v.getId() == R.id.et_pwd2) {
			if (actionId == EditorInfo.IME_ACTION_DONE && mBtCommit.isEnabled()) {
				resetPwdRequest();
			}
		}
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
		case R.id.btn_commit:
			if (mEtPassword1.checkText() != Error.NONE) {
				showToastMsg(mEtPassword1.getErrorText());
			} else if (mEtPassword2.checkText() != Error.NONE) {
				showToastMsg(mEtPassword2.getErrorText());
			} else if(!checkCommit()){
				showToastMsg(R.string.forget_reset_passwrod_error);
			}else{
				resetPwdRequest();
			}
			break;
		}
    }
	
	private void resetPwdRequest() {
		showWaitDialog(RequestInfo.FORGET_RESET_PWD);
		ResetPwd.Params params = new ResetPwd.Params();

        params.mobile = getIntent().getStringExtra(ProductIntent.EXTRA_MOBILE);
        String pwd = mEtPassword1.getText().toString();
        pwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(pwd) : pwd;
        params.password = pwd;

        ResetPwd.doRequest(params,ForgetPwdResetActivity.this);
    }

//	private void loginRequest() {
//		showWaitDialog(RequestInfo.LOGIN);
//        Login.Params params = new Login.Params();
//
//        params.logInID = getIntent().getStringExtra(ProductIntent.EXTRA_MOBILE);
//        String pwd = mEtPassword1.getText().toString();
//        pwd = GlobalConstant.isEncrypt?SHA1.SHA1Digest(pwd):pwd;
//        params.password = pwd;
//        params.type = 3;//注册后首次登录
//        params.logInType = 2;
//        params.is_register = 1;
//        params.version_client = App.instance().getVersionName();
//               
//        mPrefs = UserPrefs.get(this);
//        mPrefs.setLoginID(params.logInID);
//        mPrefs.save();
//        App.LockStartTime();
//        Login.doRequest(params, ForgetPwdResetActivity.this);
//    }
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
	    super.netFinishOk(info, res, tag);
	    if (RequestInfo.FORGET_RESET_PWD == info){
	    	ResetPwd mr = (ResetPwd)res;
	    	if (BaseRes.RESULT_OK.equals(mr.result)) {
	    		finish();
	    	} else {
	    		showToastMsg(Util.transformString(R.string.forget_reset_fail));
	    	}
	    } else if (RequestInfo.LOGIN == info){
	    	Login log = (Login)res;
			mPrefs = UserPrefs.get(this);
			mPrefs.setKEY(log.encryptedKey);
			mPrefs.setUid(log.uid);
			mPrefs.setIsNeedVerify(false);
			mPrefs.save();
			App.LockStartTime();
			
			Intent intent = new Intent(this, ProductActivity.class);
			intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		int type = 0;
		if (RequestInfo.FORGET_RESET_PWD == info
				||RequestInfo.LOGIN == info) {
			showToastMsg(msg);
			type = 0;
		} else {
			networkResultErr(msg);
			type = 1;
		}
		hideWaitDialog(info);
		return type;
	}
	
	@Override
	public void onCheckEditTextChanged(Error error) {
		mBtCommit.setEnabled((mEtPassword1.checkText() != Error.EMPTY) 
				&& (mEtPassword2.checkText() != Error.EMPTY));
	}
	
	/*class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {  
		@Override  
		public CharSequence getTransformation(CharSequence source, View view) {  
		    return new PasswordCharSequence(source);  
		}  
		   
		private class PasswordCharSequence implements CharSequence {  
		    private CharSequence mSource;  
		    public PasswordCharSequence(CharSequence source) {  
		        mSource = source; // Store char sequence  
		    }  
		    public char charAt(int index) {  
		        return '*'; // This is the important part  
		    }  
		    public int length() {  
		        return mSource.length(); // Return default  
		    }  
		    public CharSequence subSequence(int start, int end) {  
		        return mSource.subSequence(start, end); // Return default  
		    }  
		}  
	}*/
	
}
