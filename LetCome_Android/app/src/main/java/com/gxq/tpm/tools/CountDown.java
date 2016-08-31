package com.gxq.tpm.tools;

import android.os.SystemClock;

public class CountDown extends TimeTimer {
	private long mStartTime;
	private long mInterval;
	private int mMax;
	private int mCount;
	
	private OnCountDownListener mListener;
	
	public CountDown(int maxNum){
		this.mMax = maxNum;
	}

	@Override
	public void startTimer(long delay, long interval) {
		long realTime = SystemClock.elapsedRealtime();
		long diffTime = realTime - mStartTime;
		
		if (mStartTime > 0 && diffTime <= mMax * mInterval * INTERVAL_SECOND && diffTime > 0) {
			if (this.mInterval != interval) { // 如果interval不相等，从新计数
				mCount = 0;
				mStartTime = realTime;
				this.mInterval = interval;
			} else { // 根据时间计算count
				mCount = (int) (diffTime / (mInterval * INTERVAL_SECOND)); 
			}
		} else {
			mCount = 0;
			mStartTime = realTime;
			this.mInterval = interval;
		}
		
		super.startTimer(delay, interval);
	}
	
	@Override
	protected void handle() {
		if (mListener != null) {
			if (mCount >= mMax) {
				mListener.onCountDownFinished();
				internalStopTimer();
			} else {
				mListener.onCountDownRun(mMax, mMax - mCount);
				mCount++;
			}
		}
	}
    
	public void setOnCountDownListener(OnCountDownListener listener) {
		this.mListener = listener;
	}
	
	public static interface OnCountDownListener {
		void onCountDownFinished();
		void onCountDownRun(int maxNum, int remainNum);
	}
	
}
