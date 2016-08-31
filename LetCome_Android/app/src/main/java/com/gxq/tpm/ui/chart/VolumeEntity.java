package com.gxq.tpm.ui.chart;

public class VolumeEntity implements IStickEntity {
	private long volume;
	private double open;
	private double close;
	private String date;

	public VolumeEntity() {
		super();
	}

	public VolumeEntity(long volume, double open, double close, String date) {
		super();
		this.volume = volume;
		this.open = open;
		this.close = close;
		this.date = date;
	}

	/**
	 * @return the volume
	 */
	public long getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 */
	public void setVolume(long volume) {
		this.volume = volume;
	}

	/**
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * @param open
	 */
	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * @param close
	 */
	public void setClose(double close) {
		this.close = close;
	}

	@Override
	public double getHigh() {
		return volume;
	}

	@Override
	public double getLow() {
		return 0;
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
