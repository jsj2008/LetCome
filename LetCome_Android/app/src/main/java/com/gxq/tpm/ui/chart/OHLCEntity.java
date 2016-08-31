package com.gxq.tpm.ui.chart;

public class OHLCEntity implements IStickEntity {
	private double open;
	private double high;
	private double low;
	private double close;
	private String date;

	public OHLCEntity(double open, double high, double low, double close,
			String date) {
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;
	}

	public OHLCEntity() {
		super();
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

	@Override
	public double getHigh() {
		return high;
	}

	/**
	 * @param high
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	@Override
	public double getLow() {
		return low;
	}

	/**
	 * @param low
	 */
	public void setLow(double low) {
		this.low = low;
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
	public String getDate() {
		return date;
	}

	@Override
	public void setDate(String date) {
		this.date = date;
	}
	
}
