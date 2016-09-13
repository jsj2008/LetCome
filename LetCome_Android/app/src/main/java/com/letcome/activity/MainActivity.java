package com.letcome.activity;

import android.content.Intent;
import android.os.Bundle;

import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.tools.DispatcherTimer;
import com.letcome.R;
import com.letcome.fragement.MeFragment;

public class MainActivity extends SuperActivity {

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

//		int activityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
//		if (activityFrom == ProductIntent.FROM_LAUNCH) {
//			getIntent().putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
//
//			mFromLaunch = true;
//		}

		mNeedNoticeDispatcher = initNewTimer(5 * 60);

		changeFragment(R.id.tab_me, getIntent().getExtras());


	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

//		int fragment = getIntent().getIntExtra(ProductIntent.EXTRA_WHICH_FRAGMENT, mCurrFragment);
//		changeFragment(fragment, getIntent().getExtras());
//
//		int activityFrom = getIntent().getIntExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
//		if (activityFrom == ProductIntent.FROM_LAUNCH) {
//			getIntent().putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, 0);
//
//			mFromLaunch = true;
//		}

	}

	@Override
	protected void initBar() {
		super.initBar();
		getTabBar().newTabBarItem(R.id.tab_me, R.drawable.tabbar_me);
		getTabBar().newTabBarItem(R.id.tab_categories, R.drawable.tabbar_categories);
		getTabBar().newTabBarItem(R.id.tab_sell, R.drawable.tabbar_sell);
		getTabBar().newTabBarItem(R.id.tab_chat, R.drawable.tabbar_chat);
		getTabBar().newTabBarItem(R.id.tab_profile, R.drawable.tabbar_profile);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mNeedNoticeDispatcher.timerTaskControl(true);

//		if (mFromLaunch && mUserPrefs.hasUserLogin()) {
//			Intent intent = new Intent(this, LoginPrepareActivity.class);
//			startActivity(intent);
//		}
		mFromLaunch = false;
	}

	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		requestMsgNotice();
	}

	private void requestMsgNotice() {
//		if (mUserPrefs.hasUserLogin()) {
//			NeedNotice.Params params = new NeedNotice.Params();
//			params.type = NeedNotice.MSG;
//			params.from_time = mUserPrefs.getProductMsgTime();
//			NeedNotice.doRequest(params, this, NEED_NOTICE_MSG);
//		}
	}

	private void requestGetTime() {
//		if (mUserPrefs.hasUserLogin()) {
//			GetTime.doRequest(this);
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mNeedNoticeDispatcher.timerTaskControl(false);
	}

	@Override
	public FragmentBase createFragmentById(int markId) {
		FragmentBase fragment = new FragmentBase();
		switch (markId) {
			case R.id.tab_me:
				fragment = new MeFragment(markId);
				break;
//			case R.id.tab_strategy:
//				fragment = new StrategyFragment();
//				break;
//			case R.id.tab_account:
//				fragment = new SettlementFragment(markId);
//				break;
//			case R.id.tab_mine:
//				fragment = new ProductMineFragment(markId);
//				break;
		}
		return fragment;
	}

	@Override
	public void onTabClick(final int id) {
		if (id == R.id.tab_profile){
			gotoLogin();
		}else{
			super.onTabClick(id);
		}
	}

	@Override
	protected void onTabChanged(int id) {
		super.onTabChanged(id);

		switch (id) {
			case R.id.tab_me:
//				getTitleBar().hideTitleBar();
				getTitleBar().setTitle(R.string.app_name);
				getTabBar().selectTabItem(R.id.tab_me);

				break;
			case R.id.tab_categories:
//				getTitleBar().hideTitleBar();
				getTitleBar().setTitle(R.string.tab_categories_text);
				getTabBar().selectTabItem(R.id.tab_categories);
				break;
			case R.id.tab_sell:
//				getTitleBar().setTitle(R.string.tab_account_text);
				getTabBar().selectTabItem(R.id.tab_sell);
				break;
			case R.id.tab_chat:
				getTitleBar().setTitle(R.string.tab_chats_text);
				getTabBar().selectTabItem(R.id.tab_chat);
				break;

			case R.id.tab_profile:
				getTitleBar().hideTitleBar();
				getTabBar().selectTabItem(R.id.tab_profile);
				requestGetTime();
				gotoLogin();
				break;
		}

	}

	void gotoLogin(){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);

	}



}
