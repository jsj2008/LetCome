package com.gxq.tpm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.launch.LockPatternActivity;
import com.gxq.tpm.prefs.LockPrefs;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.ui.CItemBar;

public class GestureSettingActiviy extends SuperActivity implements View.OnClickListener, CItemBar.OnItemBarClickListener {
	private static final String Tag = "GestureSettingActiviy";

	public static final int REQUEST_CODE_SET 	= 11;
	public static final int REQUEST_CODE_CLOSE 	= 22;
	public static final int REQUEST_CODE_MODIFY = 33;
	public static final int REQUEST_CODE_FORGET = 44;

	private Intent intent;
	private ImageView mIvGestureSwitch;
	private LinearLayout mDetail;
	private LockPrefs mLockPrefs;
	private View mgestureSwitchLine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_setting);
		
		Print.i(Tag, "onCreate");
		initUI();
		mLockPrefs = App.getUserPrefs().getLockPrefs();
	}
	
	private void initUI() {
		mIvGestureSwitch = (ImageView) findViewById(R.id.iv_gesture_switch);
		mIvGestureSwitch.setOnClickListener(this);
		mDetail = (LinearLayout) findViewById(R.id.ll_detail);
		mgestureSwitchLine =  findViewById(R.id.gesture_switch_line);
		
		((CItemBar) findViewById(R.id.lock_modify_gesture)).setOnItemBarClickListener(this);
    }

	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_gesture_pwd);
		getTitleBar().showBackImage();
	}

	@Override
    public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_gesture_switch:
			if (mLockPrefs.gestureIsOpen()) {
				intent = new Intent();
				intent.setClass(GestureSettingActiviy.this, LockPatternActivity.class);
				intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_CLOSE);
				startActivityForResult(intent, REQUEST_CODE_CLOSE);
			} else if (mLockPrefs.gestureIsSetted()) {
				mLockPrefs.openGesture();
				resetUI();
			} else {
				intent = new Intent();
				intent.setClass(GestureSettingActiviy.this, LockPatternActivity.class);
				intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_SET);
				startActivityForResult(intent, REQUEST_CODE_SET);
			}
			break;
		}
	}
	
	@Override
	public void onItemClick(CItemBar v) {
		switch (v.getId()) {
		case R.id.lock_modify_gesture:
			intent = new Intent();
			intent.setClass(GestureSettingActiviy.this, LockPatternActivity.class);
			intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_MODIFY);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE_SET == requestCode && Activity.RESULT_OK == resultCode) {
			mLockPrefs.openGesture();
		} else if (REQUEST_CODE_CLOSE == requestCode && Activity.RESULT_OK == resultCode) {
			mLockPrefs.closeGesture();
		} else if (REQUEST_CODE_FORGET == requestCode && Activity.RESULT_OK == resultCode) {
			mLockPrefs.removeGesture();
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();   
	    resetUI();
	}

	private void resetUI() {
		boolean open = mLockPrefs.gestureIsOpen();
	    mIvGestureSwitch.setImageResource(open ? R.drawable.switch_on : R.drawable.switch_off);
	    mDetail.setVisibility(open ? View.VISIBLE : View.GONE);
	    mgestureSwitchLine.setVisibility(open ? View.VISIBLE : View.GONE);
    }
	
}
