package com.gxq.tpm.ui;

import com.gxq.tpm.mode.hq.GetKLine;
import com.gxq.tpm.mode.hq.GetKLine.KStruct;
import com.gxq.tpm.ui.chart.IStickEntity;
import com.gxq.tpm.ui.chart.ListChartData;
import com.gxq.tpm.ui.chart.OHLCEntity;
import com.gxq.tpm.ui.chart.VolumeEntity;

import android.content.Context;
import android.util.AttributeSet;

public class ProductKLineChart extends CKLineChart {
	private final static int MAX_NUM = 60;
	
	public ProductKLineChart(Context context) {
		super(context);
	}
	
	public ProductKLineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void initializeChart() {
		// 最大显示足数
		mKLineChart.setDisplayNumber(MAX_NUM);
		
		// 最大显示足数
		mKLineVolumeChart.setDisplayNumber(MAX_NUM);
	}
	
	@Override
	protected String formatDate(KStruct struct) {
		long date = struct.date;
		return Long.toString(date);
	}

	public void assignKDataSingle(GetKLine data) {
		if (mKChartRecords.isEmpty() 
				|| data == null
				|| data.records.isEmpty()) {
			return;
		}
		
		GetKLine.KStruct record = data.records.get(data.records.size() - 1);
		
		if ((record.Open == 0)
				|| (record.New == 0)
				|| (record.High == 0)
				|| (record.Low == 0)) {
			record.Open = record.YClose;
			record.New = record.YClose;
			record.High = record.YClose;
			record.Low = record.YClose;
		}
		
		//update k line
		OHLCEntity entity = (OHLCEntity) mKChartRecords.get(mKChartRecords.size() - 1);
		entity.setClose(record.New);
		entity.setHigh(record.High);
		entity.setLow(record.Low);
		
		//单个成交量的更新数据
		VolumeEntity volumeEntity = (VolumeEntity) mKChartVolumes.get(mKChartVolumes.size() - 1);
		volumeEntity.setClose(record.New);
		volumeEntity.setVolume(record.Volume / 100);
		
		mKLineChart.setStickData(new ListChartData<IStickEntity>(mKChartRecords));
		mKLineChart.postInvalidate();
		
		mKLineVolumeChart.setStickData(new ListChartData<IStickEntity>(mKChartVolumes));
		mKLineVolumeChart.postInvalidate();
	}

}
