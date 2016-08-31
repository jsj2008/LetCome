package com.gxq.tpm.ui.chart;

public class MinuteEntity implements IMeasurable, IHasDate {
	private double high;
	private double low;
	private double yClose;
	private double open;
	private double curr;
	private IChartData<Float> waveData;
	private long volume;
	private boolean gain;
	private String date;
	
	public MinuteEntity(double high, double low, double yClose, double open,
			double curr, IChartData<Float> waveData, long volume, String date) {
		this.high = high;
		this.low = low;
		this.yClose = yClose;
		this.open = open;
		this.curr = curr;
		this.waveData = waveData;
		this.volume = volume;
		this.date = date;
	}
	
	public MinuteEntity() {
		super();
	}
	
	public IChartData<Float> getWaveData() {
		return waveData;
	}
	
	public void setWaveData(IChartData<Float> waveData) {
		this.waveData = waveData;
	}
	
	public double getYClose() {
		return yClose;
	}
	
	public void setYClose(double yClose) {
		this.yClose = yClose;
	}
	
	public double getOpen() {
		return this.open;
	}
	
	public void setOpen(double open) {
		this.open = open;
	}
	
	public double getCurr() {
		return this.curr;
	}
	
	public void setCurr(double curr) {
		this.curr = curr;
	}

	@Override
	public double getHigh() {
		return high;
	}
	
	public void setHigh(double high) {
		this.high = high;
	}

	@Override
	public double getLow() {
		return low;
	}
	
	public void setLow(double low) {
		this.low = low;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}
	
	public boolean isGain() {
		return gain;
	}
	
	public void setGain(boolean gain) {
		this.gain = gain;
	}

	@Override
	public String getDate() {
		return date;
	}

	@Override
	public void setDate(String date) {
		this.date = date;
	}

}
