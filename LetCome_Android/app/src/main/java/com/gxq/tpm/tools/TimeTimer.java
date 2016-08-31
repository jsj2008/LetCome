package com.gxq.tpm.tools;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

public class TimeTimer {
	public static final int INTERVAL_SECOND = 1000;
	
	private Timer mTimer;
	private TimerTask mTimerTask;
	
	private OnTimeTimerListener mListener;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			handle();
		}
	};
	
	public void startTimer() {
		startTimer(1);
	}
	
	public void startTimer(long interval) {
		startTimer(0, interval);
	}
	
	public void startTimer(long delay, long interval) {
		internalStopTimer();
		internalStartTimer();
		
		mTimer.scheduleAtFixedRate(mTimerTask, delay, interval * INTERVAL_SECOND);
	}
	
	protected void internalStartTimer(){
		mTimer = new Timer(true);
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		};
	}
	
	public void stopTimer() {
		internalStopTimer();
	}
	
	protected void internalStopTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}

		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}
	
	protected void handle() {
		if (mListener != null) {
			mListener.onTimeStep();
		}
	}
	
	public void setOnTimeTimerListener(OnTimeTimerListener listener) {
		this.mListener = listener;
	}
	
	public static interface OnTimeTimerListener {
		void onTimeStep();
	}
	
}
