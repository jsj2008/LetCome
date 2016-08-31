package com.gxq.tpm.ui;

import com.gxq.tpm.mode.hq.GetKLine.KStruct;

import android.content.Context;
import android.util.AttributeSet;

public class ProductMinuteKLineChart extends CKLineChart {
	private final static int MAX_NUM = 30;
	
	public ProductMinuteKLineChart(Context context) {
		this(context, null);
	}
	
	public ProductMinuteKLineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected String formatDate(KStruct struct) {
		String date = struct.time;
		if (date == null) date = "";
		return date;
	}

	@Override
	protected void initializeChart() {
		// 最大显示足数
		mKLineChart.setDisplayNumber(MAX_NUM);
		
		// 最大显示足数
		mKLineVolumeChart.setDisplayNumber(MAX_NUM);
	}

}
