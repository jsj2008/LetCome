package com.gxq.tpm.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import com.letcome.App;
import com.letcome.R;
import com.gxq.tpm.activity.GestureSettingActiviy;
import com.gxq.tpm.activity.ProductActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.launch.LogOut;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.GlobalInfo;
import com.gxq.tpm.ui.CItemBar;

public class UserSettingsActivity extends SuperActivity implements
			CItemBar.OnItemBarClickListener, View.OnClickListener {
	
	private CItemBar mPhoneLoginItem;
	private CItemBar mGesturePwdItem;
	private CItemBar mAboutItem;
	private TextView mQuitItem;

	@Override
	protected void initBar() {
		super.initBar();

		getTitleBar().setTitle(R.string.user_setting);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initUI();
		bindAction();
	}

	private void initUI() {
		mPhoneLoginItem = (CItemBar) findViewById(R.id.user_phone_login);
		mGesturePwdItem = (CItemBar) findViewById(R.id.user_gesture_pwd);
		mAboutItem = (CItemBar) findViewById(R.id.user_about);
		mQuitItem = (TextView) findViewById(R.id.user_quit);
	}

	private void bindAction() {
		mPhoneLoginItem.setOnItemBarClickListener(this);
		mGesturePwdItem.setOnItemBarClickListener(this);
		mAboutItem.setOnItemBarClickListener(this);
		mQuitItem.setOnClickListener(this);
	}

	@Override
	public void onItemClick(CItemBar v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.user_phone_login:
			UserInfo userInfo = App.getUserPrefs().getUserInfo();
			if (userInfo != null && userInfo.bind_mobile != null) {
				intent = new Intent(UserSettingsActivity.this, UserPhoneLoginSettingActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.user_gesture_pwd:
			intent = new Intent(UserSettingsActivity.this, GestureSettingActiviy.class);
			startActivity(intent);
			break;
		case R.id.user_about:
			intent = new Intent(UserSettingsActivity.this, UserAboutActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.user_quit) {
			LogOut.doRequest(null, UserSettingsActivity.this);
			localLogout();
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		// if (RequestInfo.LOGOUT.getOperationType().equals(operationType))
		// {
		// LogOut lo=(LogOut)res;
		// if(lo.result.equals("Y"))
		// {
		// localLogout();
		// }
		// }
	}

	private void localLogout() {
		GlobalInfo.cleanLogin();
		Intent intent = new Intent(UserSettingsActivity.this, /*LoginActivity*/ProductActivity.class);
		//  安全退出后跳转到home界面
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

}
