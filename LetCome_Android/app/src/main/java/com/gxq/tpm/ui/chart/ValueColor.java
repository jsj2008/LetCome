package com.gxq.tpm.ui.chart;

public class ValueColor {
	private String mValue;
	private int mColor;
	
	public ValueColor(String value, int color) {
		mValue = value;
		mColor = color;
	}
	
	public String getValue() {
		return mValue;
	}
	
	public int getColor() {
		return mColor;
	}
	
}
