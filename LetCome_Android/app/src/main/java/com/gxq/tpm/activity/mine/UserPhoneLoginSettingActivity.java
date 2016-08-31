package com.gxq.tpm.activity.mine;

import android.content.Intent;
import android.os.Bundle;

import com.letcome.App;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.ui.CItemBar;

public class UserPhoneLoginSettingActivity extends SuperActivity implements
		CItemBar.OnItemBarClickListener {

	private CItemBar mUserPhoneNumber;
	private CItemBar mUserModifyPwd;
	
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_phone_login_setting);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_phone_login_setting);
		
		mUserPhoneNumber = (CItemBar) findViewById(R.id.user_phone_number);
		mUserPhoneNumber.setOnItemBarClickListener(this);	
		
		mUserModifyPwd = (CItemBar) findViewById(R.id.user_modify_pwd);
		mUserModifyPwd.setOnItemBarClickListener(this);
		
		setPhoneNum();
	}

	@Override
	protected void onResume() {
		setPhoneNum();
		super.onResume();
	}

	@Override
	public void onItemClick(CItemBar v) {
		Intent intent = null;
		if (v.getId() == R.id.user_phone_number) {
			intent = new Intent(this, UserModifyPhoneActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.user_modify_pwd) {
			intent = new Intent(this, UserModifyPwdActivity.class);
			startActivity(intent);
		}
	}
	private void setPhoneNum(){
		UserInfo info = App.getUserPrefs().getUserInfo();
		if (info != null && info.bind_mobile != null) {
			mUserPhoneNumber.setContent(info.bind_mobile);
			mUserPhoneNumber.setShowDetail(true);
		}
	}
}
