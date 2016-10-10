package com.gxq.tpm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.letcome.R;
import com.gxq.tpm.fragment.FragmentBase;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.DispatcherTimer.OnDispatcherTimerListener;
import com.gxq.tpm.ui.CTabBar;
import com.gxq.tpm.ui.CTitleBar;

public class SuperActivity extends BaseActivity implements
	CTitleBar.OnTitleBarClickListener, CTabBar.OnTabBarClickListener, OnDispatcherTimerListener {
	protected static final int DEFAULT_ID = R.id.default_fragment;
	
	protected FragmentManager mFragmentManager;
	private ViewGroup mContentLayer;
	private ViewGroup mDialogLayer;
//	private ViewGroup mSpinnerLayer;
	private View mLoading, mIvLayerLoading;
	private CTitleBar mTitleBar;
	private CTabBar mTabBar;
	
	protected int mCurrFragment = -1;
	private boolean mShowingLoad = false;
	private Animation mAnimRotate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
		createParentView();
		
		mAnimRotate = AnimationUtils.loadAnimation(this, R.anim.wait_rotate);
	}
	
	private void createParentView() {
		super.setContentView(R.layout.activity_super);

		mContentLayer = (ViewGroup) findViewById(R.id.c_content);
		mDialogLayer = (ViewGroup) findViewById(R.id.dialog_layer);
		mTitleBar = (CTitleBar) findViewById(R.id.title_bar);
		mTabBar = (CTabBar) findViewById(R.id.tab_bar);
		
		initBar();
	}
	
	protected void initBar() {
		mTitleBar.setOnTitleBarClickListener(this);
		mTabBar.setOnTabBarClickListener(this);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		addCustomView(layoutResID);
	}

	@Override
	public void setContentView(View view) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}
	
	private View addCustomView(int layoutResID) {
		if (mContentLayer == null) {
			createParentView();
		} else {
			mContentLayer.removeAllViews();
		}

		getLayoutInflater().inflate(layoutResID, mContentLayer);
		return (View) mContentLayer.getParent();
	}

	public void changeFragment(int id, Bundle args) {
		if (mCurrFragment == id) {
			if (null != getCurrFragment().getArguments() && args != null && !args.isEmpty()) {
				getCurrFragment().getArguments().putAll(args);
			}
			
			return;
		}
		
		onTabChanged(id);
		
		Fragment toFragment = mFragmentManager.findFragmentByTag(String.valueOf(id));
		Fragment fromFragment = mFragmentManager.findFragmentByTag(String.valueOf(mCurrFragment));
		
		mCurrFragment = id;

		if (toFragment == null) {
			toFragment = createFragmentById(id);
			if (args != null && !args.isEmpty()) {
				toFragment.setArguments(args);
			}
		} else if (args != null && !args.isEmpty()) { //new data
			if (toFragment.getArguments() != null)
				toFragment.getArguments().putAll(args);
		}
		
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		//ft.replace(R.id.c_content, getFragment(id));
		if (fromFragment != null && !fromFragment.isHidden())
			ft.hide(fromFragment);
		
		if (!toFragment.isAdded()) {
			ft.add(R.id.c_content, toFragment/*getFragment(id)*/, String.valueOf(id));
		} else {
			ft.show(toFragment);
		}

		//ft.commit();
		ft.commitAllowingStateLoss();
		//now execute
		mFragmentManager.executePendingTransactions();
	}
	
	public Fragment getCurrFragment() {
		return mFragmentManager.findFragmentByTag(String.valueOf(mCurrFragment));
	}
	
	protected void onTabChanged(int id) {
	}
	
	public FragmentBase createFragmentById(int id) {
		return null;
	}
	
	public CTabBar getTabBar() {
		return mTabBar;
	}
	
	public CTitleBar getTitleBar() {
		return mTitleBar;
	}
	
	@Override
	public void onLeftClick(View v) {
		finish();
	}

	@Override
	public void onRightClick(View v) {
	}

	@Override
	public void onTabClick(int id) {
		changeFragment(id, getIntent().getExtras());
	}
	
	@Override
	public void onDoubleTabClick(int id) {
	}

	//replace 
	@Override
	protected void showWaitDialogD() {
		if (mLoading == null) {
			mLoading = getLayoutInflater().inflate(R.layout.loading_layout, null);
			mLoading.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			mIvLayerLoading = mLoading.findViewById(R.id.iv_layer_loading);
		}
		
		if (!mShowingLoad && mLoading != null) {
			mShowingLoad = true;
			mLoading.setVisibility(View.VISIBLE);
			mIvLayerLoading.setAnimation(mAnimRotate);
			mDialogLayer.setVisibility(View.VISIBLE);
			
			if (mLoading.getParent() == null) {
				mDialogLayer.addView(mLoading);
			}
		}
	}
	
	@Override
	protected void hideWaitDialogD() {
		if (mShowingLoad && mLoading != null) {
			mLoading.setVisibility(View.GONE);
			mIvLayerLoading.clearAnimation();
			mShowingLoad = false;
			mDialogLayer.removeView(mLoading);
			mDialogLayer.setVisibility(View.GONE);
		}
	}
	
	protected DispatcherTimer initNewTimer(int interval) {
		return initNewTimer(interval, 0);
	}
	
	protected DispatcherTimer initNewTimer(int interval, int operationType) {
		return new DispatcherTimer(this, interval, operationType);
	}
	
	@Override
	public void onAlarmClock(int operationType) {
	}
	
//	/* 不再显示app guide */
//	public void showAppGuide() {
//		if(true){
//			return;
//		}
//		ImageView guide = new ImageView(this);
//		guide.setImageResource(R.drawable.app_guide);
//		guide.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				mSpinnerLayer.removeAllViews();
//				
//				return true;
//			}
//		});
//		
//		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//				FrameLayout.LayoutParams.MATCH_PARENT);
//		
//		mSpinnerLayer.addView(guide, p);
//	}
	
}
