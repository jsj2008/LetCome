package com.gxq.tpm.tools;

import android.os.SystemClock;

public class TimeCountDown implements TimeTimer.OnTimeTimerListener {
	
	private long mStartRealTime;
	private long mStartTime;
	private long mEndTime;
	
	private OnTimeCountDownListener mListener;
	private TimeTimer mTimer;
	
	public TimeCountDown(long endTime){
		this.mEndTime = endTime;
		
		mTimer = new TimeTimer();
		mTimer.setOnTimeTimerListener(this);
	}
	
	public void startTimer(long curTime) {
		mStartRealTime = SystemClock.elapsedRealtime() / 1000;
		mStartTime = curTime;
		mTimer.startTimer();
	}
	
	@Override
	public void onTimeStep() {
		if (mListener != null) {
			long realTime = SystemClock.elapsedRealtime() / 1000;
			long curTime = mStartTime + (realTime - mStartRealTime);
			
			if (curTime >= mEndTime) {
				mListener.onTimeFinish();
			} else {
				mListener.onTimeStep(curTime);
			}
		}
	}
	
	public void stopTimer() {
		mTimer.stopTimer();
	}
	
	public void setOnTimeCountDownListener(OnTimeCountDownListener listener) {
		this.mListener = listener;
	}
	
	public static interface OnTimeCountDownListener {
		public void onTimeStep(long value);
		public void onTimeFinish();
	}
	
}
