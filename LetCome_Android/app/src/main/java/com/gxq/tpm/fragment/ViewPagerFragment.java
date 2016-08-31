package com.gxq.tpm.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxq.tpm.tools.DispatcherTimer;

public abstract class ViewPagerFragment implements DispatcherTimer.OnDispatcherTimerListener {

	protected Context mContext;
	protected View mView;
	protected ViewGroup mContainer;
	
	protected boolean mCreated;
	protected boolean mResume;
	protected boolean mShow;
	
	public ViewPagerFragment(Context context, View container) {
		this.mContext = context;
	}
	
	protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);
	
	protected void onViewCreated(View view) {
	}
	
	public void resume() {
		mResume = true;
		if (mShow) {
			start();
		}
	}
	
	public void show() {
		this.mShow = true;
		if (mResume) {
			start();
		}
	}
	
	private void start() {
		if (!mCreated) {
			mCreated = true;
			onViewCreated(getView());
			
			request();
		} else {
			timerControl(true);
		}
	}

	public View getView() {
		if (mView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			mView = onCreateView(inflater, mContainer);
		}
		return mView;
	}

	protected void request() {
	}
	
	protected void timerControl(boolean start) {
	}
	
	public void pause() {
		mResume = false;
		stop();
	}
	
	public void hide() {
		mShow = false;
		stop();
	}

	private void stop() {
		timerControl(false);
	}
	
	public boolean isCreated() {
		return mCreated;
	}
	
	@Override
	public void onAlarmClock(int type) {
	}
	
}
