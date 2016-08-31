package com.gxq.tpm.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.BaseActivity;
import com.gxq.tpm.activity.ProductActivity;
import com.gxq.tpm.adapter.LoadingPagerAdapter;
import com.gxq.tpm.tools.PageChangeListener;
import com.gxq.tpm.ui.CSliderView;

public class LoadingUPdateActivity extends BaseActivity implements View.OnClickListener {
	private final static int REQUEST_CODE_GESTURE = 1;
	
	private CSliderView mSliderView;
	private int[] instructsIds = new int[]{R.drawable.instruction1, R.drawable.instruction2, R.drawable.instruction3};

	private int mActivityFrom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_loading_update);
		
		mActivityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOnPageChangeListener(new LoadingUpdatePageChangeListener());
		
		mSliderView = (CSliderView) findViewById(R.id.slider_view);
		
		View[] views = new View[instructsIds.length];
		for (int i = 0; i < instructsIds.length; i++) {
			View view = new View(this);
			view.setBackgroundResource(instructsIds[i]);
			if (i == instructsIds.length -1) {
				view.setOnClickListener(this);
			}
			
			views[i] = view;
		}
		viewPager.setAdapter(new LoadingPagerAdapter(views));
	}
	
	@Override
	public void onClick(View v) {
		if (mActivityFrom == ProductIntent.FROM_LAUNCH) {
			mUserPrefs.setLastInstructedVersion(App.instance().getVersionName());
			
			Intent intent = new Intent();
			if (mUserPrefs.getUid() > 0 && 
					mUserPrefs.getLockPrefs().gestureIsOpen()) {
				intent.setClass(this, LockPatternActivity.class);
				intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_LOGIN);
				startActivityForResult(intent, REQUEST_CODE_GESTURE);
			} else {
				intent.setClass(this, ProductActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE_GESTURE == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				mUserPrefs.setLogin(true);
				
				Intent intent = new Intent(this, ProductActivity.class);
				intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
				startActivity(intent);
			}
			finish();
		}
	}
	
	private class LoadingUpdatePageChangeListener extends PageChangeListener {

		@Override
		public void onPageSelected(int index) {
			mSliderView.setSelect(index);
		}
	}
	
}
