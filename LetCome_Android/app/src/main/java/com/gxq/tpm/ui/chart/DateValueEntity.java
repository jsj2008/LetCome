package com.gxq.tpm.ui.chart;

public class DateValueEntity implements IHasDate {

	private String date;
	private double value;

	public DateValueEntity(double value, String date) {
		this.value = value;
		this.date = date;
	}

	@Override	
	public String getDate() {
		return date;
	}

	@Override
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
}
