package com.gxq.tpm.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.launch.ForgetPwdActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.ChangePwd;
import com.gxq.tpm.mode.mine.ChangePwd.Params;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.PwdCheckEditText;
import com.gxq.tpm.ui.CheckEditText.Error;
import com.gxq.tpm.ui.CheckEditText.OnCheckEditTextChangeListener;

public class UserModifyPwdActivity extends SuperActivity 
    implements OnClickListener,OnCheckEditTextChangeListener{
	
	private PwdCheckEditText mEtOriginalPwd,mEtNewPwd,mEtConfirmPwd;

	private Button mBtnConfirm;
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.phone_modify_pwd);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_modify_pwd);
		
		mEtOriginalPwd = (PwdCheckEditText) findViewById(R.id.et_original_pwd);
		mEtOriginalPwd.setOnCheckEditTextChangeListener(this);
		
		findViewById(R.id.tv_forget_pwd).setOnClickListener(this);
		
		mEtNewPwd = (PwdCheckEditText) findViewById(R.id.et_new_pwd);
		mEtNewPwd.setOnCheckEditTextChangeListener(this);
		mEtConfirmPwd = (PwdCheckEditText) findViewById(R.id.et_confirm_pwd);
		mEtConfirmPwd.setOnCheckEditTextChangeListener(this);
		
		mBtnConfirm = (Button) findViewById(R.id.btn_next_step);
		mBtnConfirm.setText(R.string.btn_confirm);
		mBtnConfirm.setEnabled(false);
		mBtnConfirm.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_forget_pwd) {
			Intent intent = new Intent(this, ForgetPwdActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.btn_next_step) {
			
			if(mEtOriginalPwd.checkText()!=Error.NONE){
				showToastMsg(mEtOriginalPwd.getErrorText());
			}else if(mEtNewPwd.checkText()!=Error.NONE){
				showToastMsg(mEtNewPwd.getErrorText());
			}else if(mEtConfirmPwd.checkText()!=Error.NONE){
				showToastMsg(mEtConfirmPwd.getErrorText());
			}else{
				
				String newPwd = mEtNewPwd.getText().toString();
				String confirmPwd  = mEtConfirmPwd.getText().toString();
				if (newPwd.equals(confirmPwd)) {
					String oldPwd = mEtOriginalPwd.getText().toString();
					requestResetPwd(oldPwd, newPwd);
				}else{
					showToastMsg(R.string.user_modify_pwd_not_equal);
				}
				
			}
			
		}
	}
	
	private void requestResetPwd(String oldPwd, String newPwd) {
	    ChangePwd.Params params = new Params();

	    oldPwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(oldPwd) : oldPwd;
        params.passwordOld = oldPwd;

	    newPwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(newPwd) : newPwd;
        params.passwordNew = newPwd;

	    params.uid = App.getUserPrefs().getUid();
	    params.type = 1;
	    ChangePwd.doRequest(params,this);
    }
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		if (RequestInfo.CHANGE_PWD == info) {
			ChangePwd rp = (ChangePwd) result;
			if (BaseRes.RESULT_OK.equals(rp.result)) {
				new ProductDialog.Builder(this)
					.setContentText(R.string.phone_modify_success_tips, R.drawable.layer_success)
					.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						@Override
						public void onDialogClick(int id) {
							finish();
						}
					}).create().show();				
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
		mBtnConfirm.setEnabled(mEtOriginalPwd.checkText()!=Error.EMPTY&&
				mEtNewPwd.checkText()!=Error.EMPTY&&
				mEtConfirmPwd.checkText()!=Error.EMPTY);
	}
	
}
