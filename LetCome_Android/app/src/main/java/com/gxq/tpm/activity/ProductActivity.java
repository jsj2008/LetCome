package com.gxq.tpm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.fragment.HomeFragment;
import com.gxq.tpm.fragment.ProductMineFragment;
import com.gxq.tpm.fragment.SettlementFragment;
import com.gxq.tpm.fragment.StrategyFragment;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.GetTime;
import com.gxq.tpm.mode.NeedNotice;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.DispatcherTimer;

public class ProductActivity extends SuperActivity {
	public final static int REQUESR_CODE_SET_NICKNAME 		= 99;
	public final static int REQUESR_CODE_SET_GESTURE 		= 199;
	public final static int REQUESR_CODE_UNSIGN_AGREEMENT 	= 299;
	
	public final static int NEED_NOTICE_MSG					= 1;
	public final static int NEED_NOTICE_SETTLE				= 2;
	
	private DispatcherTimer mNeedNoticeDispatcher;
	private boolean mFromLaunch;
	
	private long mMineClickTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int activityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
		if (activityFrom == ProductIntent.FROM_LAUNCH) {
			getIntent().putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
			
			mFromLaunch = true;
		}
		
		mNeedNoticeDispatcher = initNewTimer(5 * 60);
		
		changeFragment(R.id.tab_cooperation, getIntent().getExtras());
		
		//baidu push service
//		try {
//			Util.bindPushService(this);
//		} catch (Exception e) {
//			Print.e("bindPushService", "bindPushService error");
//		}
//		
//		if (getIntent().getBooleanExtra(ProductIntent.EXTRA_SET_GESTURE, false)
//				&& !App.getUserPrefs().getLockPrefs().gestureIsSetted()) {
//			Intent intent = new Intent(this, LockPatternActivity.class);
//			intent.putExtra(ProductIntent.EXTRA_TYPE, LockPatternActivity.GESTURE_SET);
//			startActivity(intent);
//		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		
		int fragment = getIntent().getIntExtra(ProductIntent.EXTRA_WHICH_FRAGMENT, mCurrFragment);
		changeFragment(fragment, getIntent().getExtras());
		
		int activityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
		if (activityFrom == ProductIntent.FROM_LAUNCH) {
			getIntent().putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
			
			mFromLaunch = true;
		}
		
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		getTabBar().newTabBarItem(R.id.tab_cooperation, R.string.tab_cooperation_text, R.drawable.tabbar_cooperation);
		getTabBar().newTabBarItem(R.id.tab_strategy, R.string.tab_strategy_text, R.drawable.tabbar_strategy);
		getTabBar().newTabBarItem(R.id.tab_account, R.string.tab_account_text, R.drawable.tabbar_account);
		getTabBar().newTabBarItem(R.id.tab_mine, R.string.tab_mine_text, R.drawable.tabbar_mine);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mNeedNoticeDispatcher.timerTaskControl(true);
		
		if (mFromLaunch && mUserPrefs.hasUserLogin()) {
			Intent intent = new Intent(this, LoginPrepareActivity.class);
			startActivity(intent);
		}
		mFromLaunch = false;
	}
	
	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		requestMsgNotice();
	}
	
	private void requestMsgNotice() {
		if (mUserPrefs.hasUserLogin()) {
			NeedNotice.Params params = new NeedNotice.Params();
			params.type = NeedNotice.MSG;
			params.from_time = mUserPrefs.getProductMsgTime();
			NeedNotice.doRequest(params, this, NEED_NOTICE_MSG);
		}
	}
	
	private void requestGetTime() {
		if (mUserPrefs.hasUserLogin()) {
			GetTime.doRequest(this);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mNeedNoticeDispatcher.timerTaskControl(false);
	}
	
	@Override
	public FragmentBase createFragmentById(int markId) {
		FragmentBase fragment = null;
		switch (markId) {
		case R.id.tab_cooperation:
			fragment = new HomeFragment(markId);
			break;
		case R.id.tab_strategy:
			fragment = new StrategyFragment();
			break;
		case R.id.tab_account:
			fragment = new SettlementFragment(markId);
			break;
		case R.id.tab_mine:
			fragment = new ProductMineFragment(markId);
			break;
		}
		return fragment;
	}
	
	@Override
	public void onTabClick(final int id) {
		if (!mUserPrefs.hasUserLogin()) {
			if (id == R.id.tab_strategy || id == R.id.tab_account || id == R.id.tab_mine) {
				showLoginActivity(new LoginCallback() {
					@Override
					public void login() {
						onTabClick(id);
					}
					@Override
					public void cancel() {
						onTabChanged(mCurrFragment);
					}
					@Override
					public boolean isStrategy() {
						return false;
					}
				});
				return;
			}
		}
		super.onTabClick(id);	
	}
	
	@Override
	protected void onTabChanged(int id) {
		super.onTabChanged(id);
		
		switch (id) {
		case R.id.tab_cooperation:
			getTitleBar().hideTitleBar();
			getTitleBar().setTitle(R.string.tab_cooperation_title);
			getTabBar().selectTabItem(R.id.tab_cooperation);
			
			break;
		case R.id.tab_strategy:
			getTitleBar().hideTitleBar();
			getTitleBar().setTitle(R.string.tab_strategy_title);
			getTabBar().selectTabItem(R.id.tab_strategy);
			break;
		case R.id.tab_account:
			getTitleBar().setTitle(R.string.tab_account_text);
			getTabBar().selectTabItem(R.id.tab_account);
			break;
		case R.id.tab_mine:
			getTitleBar().hideTitleBar();
			getTabBar().selectTabItem(R.id.tab_mine);

			requestGetTime();
			break;
		}
			
	}
	
	private void saveProductTime() {
		if (mUserPrefs.hasUserLogin()) {
			long clickTime = SystemClock.elapsedRealtime();
			long time = mUserPrefs.getProductMsgTime() + (clickTime - mMineClickTime) / 1000;
			mUserPrefs.setProductMsgTime(time);
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (RequestInfo.MSG_NEED_NOTICE == info) {
			NeedNotice needNotice = (NeedNotice) res;
			
			if (needNotice.records != null) {
				if (tag == NEED_NOTICE_MSG) {
					if (mCurrFragment == R.id.tab_mine) {
						getTabBar().setTabItem(R.id.tab_mine, false);
						requestGetTime();
					} else {
						getTabBar().setTabItem(R.id.tab_mine, 
								needNotice.records.get(NeedNotice.MSG) > 0);
					}
				}
			}
		} else if (RequestInfo.GET_TIME == info) {
			GetTime time = (GetTime) res;
			mUserPrefs.setProductMsgTime(time.time);
			getTabBar().setTabItem(R.id.tab_mine, false);
			
			mMineClickTime = SystemClock.elapsedRealtime();
		}
	}

}
