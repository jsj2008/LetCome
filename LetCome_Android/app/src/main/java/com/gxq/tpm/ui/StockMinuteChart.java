package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.mode.hq.GetMinuteInfo;
import com.gxq.tpm.ui.chart.ListChartData;
import com.gxq.tpm.ui.chart.MinuteChart;
import com.gxq.tpm.ui.chart.MinuteEntity;
import com.gxq.tpm.ui.chart.MinuteVolumeStickChart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class StockMinuteChart extends LinearLayout {
	private static final int DEFAULT_MAX_DISPLAY_NUM = 241;
	
	protected MinuteChart mMinuteChart;
	protected MinuteVolumeStickChart mMinuteVolumeChart;
	
	protected CStockHandicapView mSellHandicapView;
	protected CStockHandicapView mBuyHandicapView;
	
	private GetMinuteInfo mMinuteInfo;
	
	public StockMinuteChart(Context context) {
		this(context, null);
	}
	
	public StockMinuteChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.chart_minute_handicap, this, true);
		
		mMinuteChart = (MinuteChart) findViewById(R.id.chart_minute);
		mMinuteVolumeChart = (MinuteVolumeStickChart) findViewById(R.id.chart_minute_v);
		
		mSellHandicapView = (CStockHandicapView) findViewById(R.id.stock_chart_handicap_sell);
		mBuyHandicapView = (CStockHandicapView) findViewById(R.id.stock_chart_handicap_buy);
		
		mSellHandicapView.setSell(true);
		mBuyHandicapView.setSell(false);
		
		mMinuteChart.initChart();
		mMinuteVolumeChart.initChart();
		
		initializeChart();
	}
	
	protected void initializeChart() {
		List<String> titleBottom = new ArrayList<String>();
		titleBottom.add("9:30");
		titleBottom.add("11:30/13:00");
		titleBottom.add("15:00");
		mMinuteChart.setBottomTitles(titleBottom, new int[]{1, 1});
		mMinuteChart.setMaxDisplayNumber(DEFAULT_MAX_DISPLAY_NUM);
		
		mMinuteVolumeChart.setMaxDisplayNumber(DEFAULT_MAX_DISPLAY_NUM);
	}
	
	public void assignMinuteData(GetMinuteInfo minuteData) {
		if (minuteData == null) {
			return;
		}
		
		mMinuteInfo = minuteData;
		
		MinuteEntity data = new MinuteEntity();
		ListChartData<Float> records = new ListChartData<Float>();

		float min = minuteData.Yclose, max = minuteData.Yclose;
		if (minuteData.records.size() != 0) {
			min = max = minuteData.records.get(0)[GetMinuteInfo.NEW];
		}
		for (float[] f : minuteData.records) {
			if (f[GetMinuteInfo.NEW] < min) {
				min = f[GetMinuteInfo.NEW];
			} else if (f[GetMinuteInfo.NEW] > max) {
				max = f[GetMinuteInfo.NEW];
			}
			records.add(f[GetMinuteInfo.NEW]);
		}
		
		if (minuteData.New > 0) {
		} else {
			minuteData.Open = minuteData.New = minuteData.Yclose;
		}
		data.setWaveData(records);
		data.setOpen(minuteData.Open);
		data.setCurr(minuteData.New);
		data.setYClose(minuteData.Yclose);
		data.setHigh(max);
		data.setLow(min);
		
		mMinuteChart.setMinuteData(data);
		mMinuteChart.postInvalidate();
		
		//分时的成交量
		ListChartData<MinuteEntity> mMinuteVolumes = new ListChartData<MinuteEntity>();
		float original = minuteData.Yclose;
		for (float[] f : minuteData.records) {
			MinuteEntity entity = new MinuteEntity();
			entity.setVolume((long) f[GetMinuteInfo.VOLUME] / 100);
			entity.setGain(f[GetMinuteInfo.NEW] >= original);
			original = f[GetMinuteInfo.NEW];
			entity.setYClose(minuteData.Yclose);
			entity.setCurr(f[GetMinuteInfo.NEW]);
			mMinuteVolumes.add(entity);
		}
		mMinuteVolumeChart.setStickData(mMinuteVolumes);
		mMinuteVolumeChart.postInvalidate();
	}
	
	public void assignHandicapInfo(GetHandicapInfo handicapInfo) {
		mSellHandicapView.assignHandicap(handicapInfo);
		mBuyHandicapView.assignHandicap(handicapInfo);

		if (mMinuteInfo != null && mMinuteInfo.records.size() < DEFAULT_MAX_DISPLAY_NUM 
				&& mMinuteInfo.records.size() > 1 && handicapInfo.New > 0) {
			
			MinuteEntity data = new MinuteEntity();
			ListChartData<Float> records = new ListChartData<Float>();

			float min = mMinuteInfo.Yclose, max = mMinuteInfo.Yclose;
			if (mMinuteInfo.records.size() != 0) {
				min = max = mMinuteInfo.records.get(0)[GetMinuteInfo.NEW];
			}
			for (float[] f : mMinuteInfo.records) {
				if (f[GetMinuteInfo.NEW] < min) {
					min = f[GetMinuteInfo.NEW];
				} else if (f[GetMinuteInfo.NEW] > max){
					max = f[GetMinuteInfo.NEW];
				}
				records.add(f[GetMinuteInfo.NEW]);
			}
			
			if (handicapInfo.New > max) {
				max = handicapInfo.New;
			} else if (handicapInfo.New < min) {
				min = handicapInfo.New;
			}
 			
			data.setWaveData(records);
			data.setOpen(mMinuteInfo.Open);
			data.setCurr(handicapInfo.New);
			data.setYClose(mMinuteInfo.Yclose);
			data.setHigh(max);
			data.setLow(min);
			
			mMinuteChart.setMinuteData(data);
			mMinuteChart.postInvalidate();
		}
	}
	
}
