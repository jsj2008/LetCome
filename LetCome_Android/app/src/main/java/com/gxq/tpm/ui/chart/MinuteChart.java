package com.gxq.tpm.ui.chart; 

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.util.AttributeSet;

import com.letcome.R;
import com.gxq.tpm.tools.NumberFormat;

public class MinuteChart extends GridChart { 
	private static final int LEFT_RIGHT_TITLE_COUNT = 9;
	
	protected int mMaxDisplayNum = 0; 
	
	protected int mGainColor;
	protected int mLossColor;
	protected int mTitleColor;
	
	protected int mWaveColor;
	protected int mFillColor;
	protected float mWaveWidth;
	
	protected int mLastPointColor;
	protected int mLastPointRadius;
	protected int mLastPointWidth;
	
	protected double mMaxValue;
	protected double mMinValue;
	
	protected String mLeftLatitudeFormat;
	protected String mRightLatitudeFormat;
	
	protected float mValueRatio;

	protected MinuteEntity mMinuteData;
	protected IChartData<PriceSequence> mWaveData;

	public MinuteChart(Context context) {
		this(context, null);
	}

	public MinuteChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initChart() {
		super.initChart();
		mDashLatitude = false;
		
		mGainColor = getColor(R.color.gain_color);
		mLossColor = getColor(R.color.loss_color);
		mTitleColor = getColor(R.color.text_color_sub);
		
		mWaveColor = getColor(R.color.text_color_link);
		mFillColor = getColor(R.color.minute_chart_wave_bg);
		mWaveWidth = getDimension(R.dimen.horizontal_line_separate_height);
		
		mLastPointColor= getColor(R.color.gain_color);
		mLastPointRadius = getDimension(R.dimen.margin_xhdpi_2);
		mLastPointWidth = getDimension(R.dimen.horizontal_line_separate_height);
		
		mLeftLatitudeFormat = "0.00";
		mRightLatitudeFormat = "0.00";
		
		mValueRatio = 0.25f;
		
		mTopTitleQuadrantHeight = 0;
		mBottomTitleQuadrantHeight = getDimension(R.dimen.margin_xhdpi_15);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (hasDrawData()) {
			calcValueRange();
			initLatitudeTitle();
			drawWave(canvas);
			drawLastPoint(canvas);
		}
		super.onDraw(canvas);
	}

	@Override
	protected boolean hasDrawData() {
		return null != mMinuteData;
	}
	
	protected void calcValueRange() {
		double maxValue = Double.MIN_VALUE;
		double minValue = Double.MAX_VALUE;
		if (hasDrawData()) {
			maxValue = mMinuteData.getHigh();
			minValue = mMinuteData.getLow();
		}
		
		if (maxValue < minValue) {
			maxValue = 0;
			minValue = 0;
		}
		
		mMaxValue = maxValue;
		mMinValue = minValue;

		// 修正最大值和最小值
		double yClose = mMinuteData.getYClose();
		double diff = Math.max(Math.abs(mMaxValue - yClose), Math.abs(mMinValue - yClose));
		diff = diff * (1 + mValueRatio);
		
		mMaxValue = yClose + diff;
		mMinValue = yClose - diff;
		
		calcValuePostion();
	}

	private void calcValuePostion(){
		if (!hasDrawData()) {
			return;
		}
		
		if (mWaveData != null) {
			mWaveData.clear();
			mWaveData = null;
		}
		
		mWaveData = new ListChartData<PriceSequence>();
		
		float startX = mDataQuadrant.getQuadrantPaddingStartX();
		float waveWidth = mDataQuadrant.getQuadrantPaddingWidth() / (mMaxDisplayNum - 1);
		float startY = mDataQuadrant.getQuadrantPaddingStartY();
		
		IChartData<Float> datas = mMinuteData.getWaveData();
		float waveHeight = mDataQuadrant.getQuadrantPaddingHeight();
		
		float y;
		for (int i = 0; i < datas.size(); i++) {
			if (i == datas.size() - 1) {
				y = (float) ((1f - (mMinuteData.getCurr() - mMinValue)
						/ (mMaxValue - mMinValue))
						* (waveHeight) + startY);
			} else {
				y = (float) ((1f - (datas.get(i) - mMinValue)
						/ (mMaxValue - mMinValue))
						* (waveHeight) + startY);
			}
			mWaveData.add(new PriceSequence(startX + i * waveWidth, y));
		}
		
	}
	
	protected void initLatitudeTitle() { 
		if (!hasDrawData()) {
			return;
		}
		
		List<ValueColor> titleLeftX = new ArrayList<ValueColor>();
		List<ValueColor> titleRightX = new ArrayList<ValueColor>();
		
		for(int i = 0; i < LEFT_RIGHT_TITLE_COUNT; i++) {
			if (i == 0 || i == 4 || i == 8) {
				titleLeftX.add(new ValueColor(getString(R.string.number_default_value), mTitleColor));
				titleRightX.add(new ValueColor(getString(R.string.number_default_value), mTitleColor));
			} else if (i == 2 || i == 6) {
				titleLeftX.add(new ValueColor(getString(R.string.number_default_value), mTitleColor));
				titleRightX.add(new ValueColor("", mTitleColor));
			} else {
				titleLeftX.add(new ValueColor("", mTitleColor));
				titleRightX.add(new ValueColor("", mTitleColor));
			}
		}
		
		if (null != mMinuteData) {
			double diff = mMaxValue - mMinuteData.getYClose();
			titleLeftX.set(0, new ValueColor(formatAxisXL(mMinValue), mLossColor));
			titleRightX.set(0, new ValueColor("-" + formatAxisXR((diff / mMinuteData.getYClose()) * 100) + "%", mLossColor));
			
			titleLeftX.set(2, new ValueColor(formatAxisXL(mMinValue + diff / 2), mLossColor));
//			titleRightX.set(2, new ValueColor("-" + formatAxisXR((diff / 2 / mMinuteData.getYClose()) * 100) + "%", mLossColor));
			
			titleLeftX.set(4, new ValueColor(formatAxisXL(mMinuteData.getYClose()), mTitleColor));
			titleRightX.set(4, new ValueColor(formatAxisXR(0) + "%", mTitleColor));
			
			titleLeftX.set(6, new ValueColor(formatAxisXL(mMaxValue - diff / 2), mGainColor));
//			titleRightX.set(6, new ValueColor("+" + formatAxisXR((diff / 2 / mMinuteData.getYClose()) * 100) + "%", mGainColor));
			
			titleLeftX.set(8, new ValueColor(formatAxisXL(mMaxValue), mGainColor));
			titleRightX.set(8, new ValueColor("+" + formatAxisXR((diff / mMinuteData.getYClose()) * 100) + "%", mGainColor));
		}
		
		super.setLatitudeLeftTitles(titleLeftX);
		super.setLatitudeRightTitles(titleRightX);
	} 
	
	public String formatAxisXL(double value) {
		return NumberFormat.decimalFormat(mLeftLatitudeFormat, value);
	}
	
	public String formatAxisXR(double value) {
		return NumberFormat.decimalFormat(mRightLatitudeFormat, value);
	}
	
	protected void drawWave(Canvas canvas) { 
		if (null == mWaveData || mWaveData.size() <=0) { 
			return; 
		} 
		
		Paint linePaint = new Paint(); 
		linePaint.setAntiAlias(true); 
		linePaint.setStyle(Paint.Style.STROKE); 
		linePaint.setColor(mWaveColor); 
		linePaint.setStrokeWidth(mWaveWidth);
		
		Paint fillPaint = new Paint(); 
		fillPaint.setAntiAlias(true); 
		fillPaint.setStyle(Paint.Style.FILL); 
		fillPaint.setColor(mFillColor);
		
		Path linePath = new Path();
		Path fillPath = new Path();
		linePath.moveTo(mWaveData.get(0).x, mWaveData.get(0).y);
		fillPath.moveTo(mWaveData.get(0).x, mWaveData.get(0).y);
		for (int i = 1; i < mWaveData.size(); i++) {
			linePath.lineTo(mWaveData.get(i).x, mWaveData.get(i).y);
			fillPath.lineTo(mWaveData.get(i).x, mWaveData.get(i).y);
		}
		
		fillPath.lineTo(mWaveData.get(mWaveData.size() - 1).x, mDataQuadrant.getQuadrantPaddingEndY());
		fillPath.lineTo(mDataQuadrant.getQuadrantPaddingStartX(), mDataQuadrant.getQuadrantPaddingEndY());
		fillPath.lineTo(mWaveData.get(0).x, mWaveData.get(0).y);
		
		fillPath.setFillType(FillType.EVEN_ODD);
		
		canvas.drawPath(linePath, linePaint);
		canvas.drawPath(fillPath, fillPaint);
		
		Paint curPricePaint = new Paint();
		curPricePaint.setAntiAlias(true);
		curPricePaint.setStyle(Style.STROKE);
		curPricePaint.setColor(mLastPointColor);
		curPricePaint.setStrokeWidth(mLastPointWidth);
		float y = mWaveData.get(mWaveData.size() - 1).y;
		canvas.drawLine(mDataQuadrant.getQuadrantPaddingStartX(), y, 
				mDataQuadrant.getQuadrantPaddingEndX(), y, curPricePaint);
	}
	
	protected void drawLastPoint(Canvas canvas){
		if (null == mWaveData || mWaveData.size() <= 1 || mWaveData.size() >= mMaxDisplayNum) { 
			return; 
		}
		
		Paint lastPointPaint = new Paint();
		lastPointPaint.setAntiAlias(true);
		lastPointPaint.setStyle(Style.FILL);
		lastPointPaint.setColor(mLastPointColor);
		
		float x = mWaveData.get(mWaveData.size() - 1).x;
		float y = mWaveData.get(mWaveData.size() - 1).y;
		
		canvas.drawCircle(x, y, mLastPointRadius, lastPointPaint);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setColor(mLastPointColor);
		paint.setStrokeWidth(mLastPointWidth);
		canvas.drawCircle(x, y, mLastPointRadius * 2, paint);
	}
	
	/**
	 * @param maxDisplayNumber
	 */
	public void setMaxDisplayNumber(int maxDisplayNumber) { 
		mMaxDisplayNum = maxDisplayNumber; 
	} 

	/**
	 * @param waveColor
	 */
	public void setWaveColor(int waveColor) {
		this.mWaveColor = waveColor;
	}
	
	/**
	 * @param fillColor
	 */
	public void setFillColor(int fillColor) {
		mFillColor = fillColor;
	}
	
	/**
	 * @param waveWidth
	 */
	public void setWaveWidth(float waveWidth) {
		mWaveWidth = waveWidth;
	}
	
	/**
	 * @param lastPointColor
	 */
	public void setLastPointColor(int lastPointColor) {
		this.mLastPointColor = lastPointColor;
	}
	
	/**
	 * @param lastPointRadius
	 */
	public void setLastPointRadius(int lastPointRadius) {
		this.mLastPointRadius = lastPointRadius;
	}
	
	/**
	 * @param leftLatitudeFormat
	 */
	public void setLeftLatitudeFormat(String leftLatitudeFormat) {
		this.mLeftLatitudeFormat = leftLatitudeFormat;
	}
	
	/**
	 * @param rightLatitudeFormat
	 */
	public void setRightLatitudeFormat(String rightLatitudeFormat) {
		this.mRightLatitudeFormat = rightLatitudeFormat;
	}
	
	/**
	 * @param lastPointWidth
	 */
	public void setLastPointWidth(int lastPointWidth) {
		this.mLastPointWidth = lastPointWidth;
	}
	
	/**
	 * @param valueRatio
	 */
	public void setValueRatio(float valueRatio) {
		this.mValueRatio = valueRatio;
	}
	
	/**
	 * @param minuteData
	 */
	public void setMinuteData(MinuteEntity minuteData) { 
		mMinuteData = minuteData; 
	} 
	
	protected class PriceSequence {
		public PriceSequence(float x, float y){
			this.x = x;
			this.y = y;
		}
		public float x, y;
	}
} 