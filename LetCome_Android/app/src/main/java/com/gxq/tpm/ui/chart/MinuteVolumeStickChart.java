package com.gxq.tpm.ui.chart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.letcome.R;

public class MinuteVolumeStickChart extends GridChart {
	protected double mMaxValue;

	protected IChartData<MinuteEntity> mStickData;

	protected int mMaxDisplayNum;
	protected int mGainStickColor;
	protected int mLossStickColor;
	protected int mTitleColor;
	protected float mStickSpacing = 0;
	
	public MinuteVolumeStickChart(Context context) {
		super(context);
	}

	public MinuteVolumeStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void initChart() {
		super.initChart();

		mGainStickColor = getColor(R.color.gain_color);
		mLossStickColor = getColor(R.color.loss_color);
		mTitleColor = getColor(R.color.text_color_sub);
		
		mTopTitleQuadrantHeight = 0;
		mBottomTitleQuadrantHeight = 0;
		mStickSpacing = 1f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (hasDrawData()) {
			calcValueRange();
		}
		
		super.onDraw(canvas);
		
		if (hasDrawData()) {
			drawSticks(canvas);
		}
	}
	
	@Override
	protected boolean hasDrawData() {
		return mStickData != null && mStickData.size() > 0;
	}
	
	private void calcValueRange() {
		long maxValue = ((MinuteEntity) mStickData.get(0)).getVolume();
		for (int i = 0; i < mMaxDisplayNum && i < mStickData.size(); i++) {
			MinuteEntity stick = mStickData.get(i);
			if (stick.getVolume() > maxValue) {
				maxValue = stick.getVolume();
			}
		}

		this.mMaxValue = maxValue;
		
		List<ValueColor> latitudeTitles = new ArrayList<ValueColor>();
		latitudeTitles.add(new ValueColor(Long.toString(0), mTitleColor));
		latitudeTitles.add(new ValueColor(Long.toString(maxValue), mTitleColor));
		setLatitudeLeftTitles(latitudeTitles);
	}

	protected void drawSticks(Canvas canvas) {
		if (mStickData == null) {
			return;
		}

		Paint mPaint = new Paint();

		float stickWidth = mDataQuadrant.getQuadrantPaddingWidth() / mMaxDisplayNum;
		float stickX = mDataQuadrant.getQuadrantPaddingStartX();

		float maxY = mDataQuadrant.getQuadrantPaddingEndY();
		
		for (int i = 0; i < mMaxDisplayNum && i < mStickData.size(); i++) {
			MinuteEntity minuteEntity = (MinuteEntity) mStickData.get(i);
			if (minuteEntity.getVolume() < 0) {
				minuteEntity.setVolume(0);
			}
			float volumeY = (float) (1f - minuteEntity.getVolume() / mMaxValue)
					* (mDataQuadrant.getQuadrantPaddingHeight()) + mDataQuadrant.getQuadrantPaddingStartY();

			mPaint.setColor(minuteEntity.isGain() ? mGainStickColor : mLossStickColor);
			canvas.drawRect(stickX, volumeY, stickX + stickWidth - mStickSpacing, maxY, mPaint);
			stickX = stickX + stickWidth;
		}
	}

	/**
	 * @param stickSpacing
	 */
	public void setStickSpacing(float stickSpacing) {
		this.mStickSpacing = stickSpacing;
	}
	
	/**
	 * @param maxDisplayNum
	 */
	public void setMaxDisplayNumber(int maxDisplayNum) {
		mMaxDisplayNum = maxDisplayNum;
	}

	/**
	 * @param stickData
	 */
	public void setStickData(IChartData<MinuteEntity> stickData) {
		this.mStickData = stickData;
	}

}
