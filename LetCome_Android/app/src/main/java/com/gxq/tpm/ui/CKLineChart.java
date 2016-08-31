package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;
import com.gxq.tpm.mode.hq.GetFiveKLine;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.mode.hq.GetKLine;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.chart.DateValueEntity;
import com.gxq.tpm.ui.chart.IStickEntity;
import com.gxq.tpm.ui.chart.LineEntity;
import com.gxq.tpm.ui.chart.ListChartData;
import com.gxq.tpm.ui.chart.OHLCEntity;
import com.gxq.tpm.ui.chart.ScrollableCandleStickChart;
import com.gxq.tpm.ui.chart.ScrollableVolumeStickChart;
import com.gxq.tpm.ui.chart.VolumeEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public abstract class CKLineChart extends LinearLayout {
	
	protected ScrollableCandleStickChart mKLineChart;
	protected ScrollableVolumeStickChart mKLineVolumeChart;
	
	protected List<IStickEntity> mKChartRecords = new ArrayList<IStickEntity>();
	protected List<IStickEntity> mKChartVolumes = new ArrayList<IStickEntity>();
	protected List<LineEntity<DateValueEntity>> mMALineDatas = new ArrayList<LineEntity<DateValueEntity>>();
	protected List<LineEntity<DateValueEntity>> mVolumesLineDatas = new ArrayList<LineEntity<DateValueEntity>>();
	
	public CKLineChart(Context context) {
		this(context, null);
	}
	
	public CKLineChart(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.chart_kline, this);
		
		mKLineChart = (ScrollableCandleStickChart) findViewById(R.id.stock_chart_kline);
		mKLineVolumeChart = (ScrollableVolumeStickChart) findViewById(R.id.stock_chart_kline_v);
		
		mKLineChart.setOnTranslationListener(mKLineVolumeChart);
		mKLineVolumeChart.setOnTranslationListener(mKLineChart);
		
		mKLineChart.initChart();
		mKLineVolumeChart.initChart();
	
		initializeChart();
	}
	
	protected abstract void initializeChart();
	
	public void assignHandicapInfo(GetHandicapInfo handicapInfo) {
		mKLineChart.setCurPrice(handicapInfo.New);
		mKLineChart.postInvalidate();
	}
	
	public void assignKData(GetKLine data) {
		assignKLineData(data);
		
		assignKLineVolData(data);
	}
	
	protected void assignKLineData(GetKLine data) {
		double closePrice;
		
		mKChartRecords.clear();
		List<DateValueEntity> ma5EntityList = new ArrayList<DateValueEntity>();
		List<DateValueEntity> ma10EntityList = new ArrayList<DateValueEntity>();
		List<DateValueEntity> ma20EntityList = new ArrayList<DateValueEntity>();
		List<DateValueEntity> ma60EntityList = new ArrayList<DateValueEntity>();
		
		int length = data.records.size();
		double[] fivePrice = new double[5];
		
		for(int index = 0; index < length; index++) {
			GetFiveKLine.KStruct record= data.records.get(index);
			if (record.Open == 0 || record.High == 0
					|| record.Low == 0 || record.New == 0) {
				record.Open = record.YClose;
				record.High = record.YClose;
				record.Low = record.YClose;
				record.New = record.YClose;
				closePrice = record.YClose;
			} else {
				closePrice = record.New;
			}
			
			OHLCEntity entity = new OHLCEntity(record.Open, record.High, record.Low, closePrice, "");
			
			mKChartRecords.add(entity);
			
			if (index < 4) {
				fivePrice[index] = closePrice;
			} else {
				fivePrice[index%5] = closePrice;
				double sum = 0;
				for (int i = 0; i < 5; i++) {
					sum += fivePrice[i];
				}
				ma5EntityList.add(new DateValueEntity(sum/5, ""));
			}
		}
		
		for (int i = 5; i < ma5EntityList.size(); i++) {
			DateValueEntity data1 = ma5EntityList.get(i - 5);
			DateValueEntity data2 = ma5EntityList.get(i);
			ma10EntityList.add(new DateValueEntity((data1.getValue() + data2.getValue())/2, ""));
		}
		
		for(int i = 10; i < ma10EntityList.size(); i++) {
			DateValueEntity data1 = ma10EntityList.get(i - 10);
			DateValueEntity data2 = ma10EntityList.get(i);
			ma20EntityList.add(new DateValueEntity((data1.getValue() + data2.getValue())/ 2, ""));
		}
				
		for(int i = 40; i < ma20EntityList.size(); i++) {
			DateValueEntity data1 = ma20EntityList.get(i - 40);
			DateValueEntity data2 = ma20EntityList.get(i - 20);
			DateValueEntity data3 = ma20EntityList.get(i);
			ma60EntityList.add(new DateValueEntity((data1.getValue() + data2.getValue() + data3.getValue())/3, ""));
		}
		
		mMALineDatas.clear();
		mMALineDatas.add(new LineEntity<DateValueEntity>(ma5EntityList, "MA5:",
				Util.transformColor(R.color.color_646464)));
		mMALineDatas.add(new LineEntity<DateValueEntity>(ma10EntityList, "MA10:",
				Util.transformColor(R.color.color_ffc73b)));
		mMALineDatas.add(new LineEntity<DateValueEntity>(ma20EntityList, "MA20:",
				Util.transformColor(R.color.color_32abf8)));
		mMALineDatas.add(new LineEntity<DateValueEntity>(ma60EntityList, "MA60:",
				Util.transformColor(R.color.color_c25aff)));
		
		mKLineChart.setLinesData(mMALineDatas);
		mKLineChart.setStickData(new ListChartData<IStickEntity>(mKChartRecords));
		
		mKLineChart.postInvalidate();
	}
	
	protected void assignKLineVolData(GetKLine data) {
		//成交量的数据
		mKChartVolumes.clear();
		List<DateValueEntity> ma5VolEntityList = new ArrayList<DateValueEntity>();
		List<DateValueEntity> ma10VolEntityList = new ArrayList<DateValueEntity>();
		
		int length = data.records.size();
		double[] fivePrice = new double[5];
		
		for (int index = 0; index < length; index++) {
			GetKLine.KStruct record = data.records.get(index);
			long volume = record.Volume / 100;
			String date = formatDate(record);
			
			VolumeEntity volumeEntity = new VolumeEntity(volume, record.Open, record.New, date);
			mKChartVolumes.add(volumeEntity);
			
			if (index < 4) {
				fivePrice[index] = volume;
			} else {
				fivePrice[index%5] = volume;
				double sum = 0;
				for (int i = 0; i < 5; i++) {
					sum += fivePrice[i];
				}
				ma5VolEntityList.add(new DateValueEntity(sum/5, ""));
			}
		}
		
		for (int i = 5; i < ma5VolEntityList.size(); i++) {
			DateValueEntity data1 = ma5VolEntityList.get(i - 5);
			DateValueEntity data2 = ma5VolEntityList.get(i);
			ma10VolEntityList.add(new DateValueEntity((data1.getValue() + data2.getValue())/2, ""));
		}
		
		mVolumesLineDatas.clear();
		mVolumesLineDatas.add(new LineEntity<DateValueEntity>(ma5VolEntityList, "MA5:",
				Util.transformColor(R.color.color_646464)));
		mVolumesLineDatas.add(new LineEntity<DateValueEntity>(ma10VolEntityList, "MA10:",
				Util.transformColor(R.color.color_ffc73b)));
		mKLineVolumeChart.setLinesData(mVolumesLineDatas);
		mKLineVolumeChart.setStickData(new ListChartData<IStickEntity>(mKChartVolumes));
		mKLineVolumeChart.postInvalidate();
	}
	
	protected abstract String formatDate(GetKLine.KStruct struct);

	public boolean isEmpty() {
		return mKChartRecords.isEmpty();
	}
	
	public void clear() {
		mKChartRecords.clear();
		mKChartVolumes.clear();
		
		mKLineChart.setStickData(null);
		mKLineVolumeChart.setStickData(null);
	}
	
}
