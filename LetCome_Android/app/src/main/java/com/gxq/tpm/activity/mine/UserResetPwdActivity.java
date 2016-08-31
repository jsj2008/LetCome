package com.gxq.tpm.activity.mine;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.launch.ProductLoginActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.MineResetPwd;
import com.gxq.tpm.mode.mine.MineResetPwd.Params;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.PwdCheckEditText;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class UserResetPwdActivity extends SuperActivity implements
                             OnCheckEditTextChangeListener{
	private PwdCheckEditText mResetNewPwd,mResetConfirmPwd;

	private Button mBtnConfirm;
	
	private String phonenumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		
		mResetNewPwd = (PwdCheckEditText) findViewById(R.id.et_new_pwd);
		mResetNewPwd.setOnCheckEditTextChangeListener(this);
		mResetConfirmPwd = (PwdCheckEditText) findViewById(R.id.et_confirm_pwd);
		mResetConfirmPwd.setOnCheckEditTextChangeListener(this);
		
	    phonenumber= getIntent().getStringExtra(ProductIntent.EXTRA_MOBILE);
		mBtnConfirm = (Button) findViewById(R.id.btn_next_step);
		mBtnConfirm.setText(R.string.btn_confirm);
		mBtnConfirm.setEnabled(false);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				
				if(mResetNewPwd.checkText()!=Error.NONE){
					showToastMsg(mResetNewPwd.getErrorText());
				}else if(mResetConfirmPwd.checkText()!=Error.NONE){
					showToastMsg(mResetConfirmPwd.getErrorText());
				}else{
					
					String newPwd = mResetNewPwd.getText().toString();
					String confirmPwd  = mResetConfirmPwd.getText().toString();
					if (newPwd.equals(confirmPwd)) {
						requestResetPwd(phonenumber, mResetNewPwd.getText().toString());
					}else{
						showToastMsg(R.string.user_modify_pwd_not_equal);
					}
					
				}
				
			}
		});
	}
    
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.phone_modify_pwd);
		getTitleBar().showBackImage();
	}

	private void requestResetPwd(String phonenumber, String newPwd) {
		MineResetPwd.Params params = new Params();
        
		params.PhonrNumber=phonenumber;
		
	    newPwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(newPwd) : newPwd;
        params.passwordNew = newPwd;

        MineResetPwd.doRequest(params,this);
    }
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		if (RequestInfo.RESET_PWD == info) {
			MineResetPwd rp = (MineResetPwd) result;
			if (BaseRes.RESULT_OK.equals(rp.result)) {
				Intent intent = new Intent(UserResetPwdActivity.this, ProductLoginActivity.class);
				intent.putExtra(ProductIntent.EXTRA_MOBILE, ProductIntent.FROM_MINE_RESET_PWD);
				startActivity(intent);		
			} else {
				new ProductDialog.Builder(this)
					.setContentText(R.string.user_modify_pwd_error, R.drawable.layer_remind)
					.setPositiveButton(R.string.btn_confirm, null)
					.create().show();
			}
		}
	}
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.CHANGE_PWD == info) {
			return ERROR_TOAST;
		}
		return super.netFinishError(info, what, msg, tag);
	}

	@Override
	public void onCheckEditTextChanged(Error error) {
		mBtnConfirm.setEnabled(mResetNewPwd.checkText()!=Error.EMPTY&&
				mResetConfirmPwd.checkText()!=Error.EMPTY);
	}
}
