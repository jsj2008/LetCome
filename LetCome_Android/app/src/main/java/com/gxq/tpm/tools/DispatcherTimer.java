package com.gxq.tpm.tools;

public class DispatcherTimer implements TimeTimer.OnTimeTimerListener {
	private OnDispatcherTimerListener mListener;
	private int mInterval;
	private int mType;
	
	private boolean isRunning;
	private TimeTimer mTimeTimer;
	
	public DispatcherTimer(OnDispatcherTimerListener listener, int interval){
		this(listener, interval, 0);
	}
	
	public DispatcherTimer(OnDispatcherTimerListener listener, int interval, int type){
		this.mListener = listener;
		this.mInterval = interval;
		this.mType = type;
		
		this.mTimeTimer = new TimeTimer();
		this.mTimeTimer.setOnTimeTimerListener(this);
	}
	
	public void timerTaskControl(boolean start) {
		if (start) {
			if (!isRunning) {
				mTimeTimer.stopTimer();
				isRunning = true;
				mTimeTimer.startTimer(mInterval);
			}
		} else {
			if (isRunning) {
				isRunning = false;
				mTimeTimer.stopTimer();
			}
		}
	}
	
	@Override
	public void onTimeStep() {
		if (mListener != null) {
			mListener.onAlarmClock(mType);
		}
	}

	public void setListener(OnDispatcherTimerListener listener) {
		this.mListener = listener;
	}

	public static interface OnDispatcherTimerListener {
		void onAlarmClock(int type);
	}
	
}
