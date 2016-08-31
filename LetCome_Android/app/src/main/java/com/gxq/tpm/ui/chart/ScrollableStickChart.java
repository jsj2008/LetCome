package com.gxq.tpm.ui.chart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.letcome.R;

public class ScrollableStickChart extends GridChart implements OnTranslationListener {
	
	protected int mPositiveStickBorderColor = Color.RED;
	protected int mPositiveStickFillColor = Color.RED;
	
	protected int mNegativeStickBorderColor = Color.GREEN;
	protected int mNegativeStickFillColor = Color.GREEN;
	
	protected String mTopTitleFormat;
	protected IChartData<IStickEntity> mStickData;
	protected List<LineEntity<DateValueEntity>> mLinesData;
	
	protected double mMaxValue;
	protected double mMinValue;
	
	protected int mDisplayNumber;
	protected int mDisplayNumberFrom;
	protected int mDisplayNumberBegin = -1;
	
	protected int mStickSpacing = 0;
	
	protected boolean mRecord;
	protected float mDownX;
	protected float mTouchX;
	protected float mDownY;
	protected float mTranslationX;
	protected float mTotalTranslationX;
	
	protected OnTranslationListener mTranslationListener;

	public ScrollableStickChart(Context context) {
		super(context);
	}

	public ScrollableStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void initChart() {
		super.initChart();
		
		mDisplayNumber = 30;
		mPositiveStickBorderColor = mPositiveStickFillColor = getColor(R.color.gain_color);
		mNegativeStickBorderColor = mNegativeStickFillColor = getColor(R.color.loss_color);
		
		mTopTitleQuadrantHeight = getDimension(R.dimen.margin_xhdpi_15);
		mTopTitleOffset = getDimension(R.dimen.margin_xhdpi_10);
		mBottomTitleQuadrantHeight = 0;

		mTopTitleFormat = "0.00";
		mStickSpacing = getDimension(R.dimen.margin_xhdpi_1);
	}
	
	@Override
	protected boolean hasDrawData() {
		return null != this.mStickData;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!mRecord) {
				mDownX = (int) event.getX();
				mDownY = event.getY();
				mRecord = true;
			}
			mTouchX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!mRecord) {
				mDownX = event.getX();
				mRecord = true;
			}
			mTouchX = (int) event.getX();
			mDownY = event.getY();
			mTranslationX = event.getX() - mDownX;
			
			if (mTranslationListener != null)
				mTranslationListener.onTranslation(mTranslationX);
			break;
		case MotionEvent.ACTION_UP:
			mDownY = event.getY();
			mTotalTranslationX += mTranslationX;
			mTranslationX = 0;
			if (mTotalTranslationX < 0) mTotalTranslationX = 0;

			if (mTranslationListener != null)
				mTranslationListener.onTranslation(mTranslationX, mTotalTranslationX);
			
			Log.i("Chart",  "mTranstralteX = " + mTranslationX + " mTotalTranstralteX = " + mTotalTranslationX);
			
			mRecord = false;
			break;
		}
		
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (hasDrawData()) {
			calcTopTitle();
			calcBottomTitle();
			calcStickDataValueRange();
		}
		super.onDraw(canvas);
		if (hasDrawData()) {
			drawSticks(canvas);
			drawLatitudeTitle(canvas);
		}
	}
	
	protected void calcBottomTitle() {
		if (mStickData.hasData()) {
			float stickWidth = mDataQuadrant.getQuadrantPaddingWidth() / mDisplayNumber;
			int size = mStickData.size(); 
			float startX = 0;
			
			if (size > mDisplayNumber) {
				float transtralteX = (mTotalTranslationX + mTranslationX) < 0 ? 0 : (mTotalTranslationX + mTranslationX);
				
				startX = (mDisplayNumber - size) * stickWidth + transtralteX;
				if (startX > 0) {
					mDisplayNumberBegin = 0;
				} else {
					mDisplayNumberBegin = mDisplayNumberFrom - (int) (transtralteX / stickWidth);
				}
			}
			
			List<String> bottomTitles = new ArrayList<String>();
			bottomTitles.add(mStickData.get(mDisplayNumberBegin).getDate());
			
			if (mStickData.size() > 1) {
				if (mDisplayNumber <= mStickData.size()) {
					bottomTitles.add(mStickData.get(mDisplayNumberBegin + mDisplayNumber - 1).getDate());
				} else {
					bottomTitles.add(mStickData.get(mStickData.size() - 1).getDate());
				}
			}
			setBottomTitles(bottomTitles);
		}
	}
	
	protected void calcStickDataValueRange() {
		if (mStickData.hasData()) {
			double maxValue = Double.MIN_VALUE;
			double minValue = Double.MAX_VALUE;
			
			IMeasurable first = mStickData.get(0);
			// 第一个stick为停盘的情况
			if (first.getHigh() == 0 && first.getLow() == 0) {
			} else {
				maxValue = first.getHigh();
				minValue = first.getLow();
			}
	
			for (int index = 0; index < mStickData.size(); index++) {
				IStickEntity stick = mStickData.get(index);
	
				if (stick.getLow() < minValue) {
					minValue = stick.getLow();
				}
				if (stick.getHigh() > maxValue) {
					maxValue = stick.getHigh();
				}
			}
			
			if (mLinesData != null) {
				for (int index = 0; index < mLinesData.size(); index++) {
					LineEntity<DateValueEntity> dataValueEntity = mLinesData.get(index);
					for (DateValueEntity entity : dataValueEntity.getLineData()) {
						if (entity != null) {
							if (entity.getValue() < minValue) {
								minValue = entity.getValue();
							}
							if (entity.getValue() > maxValue) {
								maxValue = entity.getValue();
							}
						}
					}
				}
			}
			if (maxValue == Double.MIN_VALUE) maxValue = 0;
			if (minValue == Double.MAX_VALUE) minValue = 0;
			
			this.mMaxValue = maxValue;
			this.mMinValue = minValue;
		}
	}
	
	protected void calcTopTitle() {
		if (mLinesData != null) {
			List<ValueColor> topTitleList = new ArrayList<ValueColor>();
			for (int index = 0; index < mLinesData.size(); index++) {
				LineEntity<DateValueEntity> dataValueEntity = mLinesData.get(index);
				if (dataValueEntity.getLineData() != null && dataValueEntity.getLineData().size() > 0) {
					int size = dataValueEntity.getLineData().size();
					String value = numberFormat(mTopTitleFormat, 
							dataValueEntity.getLineData().get(size - 1).getValue());
					topTitleList.add(new ValueColor(dataValueEntity.getTitle() + value,
							dataValueEntity.getLineColor()));
				}
			}
			setTopTitles(topTitleList);
		}
	}
	
	protected void drawSticks(Canvas canvas) {
	}
	
	protected float getChartYFromValue(double value) {
		return (float) ((1f - (value - mMinValue)/ (mMaxValue - mMinValue)) * (mDataQuadrant.getQuadrantPaddingHeight())
				+ mDataQuadrant.getQuadrantPaddingStartY());
	}
	
	// 修改图表下标
	@Override
	protected void drawBottomTitle(Canvas canvas) {
		if (mBottomTitleQuadrantHeight <= 0 || null == mBottomTitles || mBottomTitles.size() < 2) {
			return;
		}

		if (mStickData.hasData()) {
			Paint mPaintFont = new Paint();
			mPaintFont.setColor(mTitleFontColor);
			mPaintFont.setTextSize(mTitleFontSize);
			mPaintFont.setAntiAlias(true);

			FontMetrics fontMetrics = mPaintFont.getFontMetrics();
			if (mBottomTitles.size() >= 1) {
				float startX = mDataQuadrant.getQuadrantPaddingStartX() + mLatitudeTitlePadding;
				float endY = mDataQuadrant.getQuadrantEndY() + getStartYFromTop(fontMetrics, 0);

				canvas.drawText(mBottomTitles.get(0), startX, endY, mPaintFont);
				
				if (mBottomTitles.size() > 1) {
					float x = mDataQuadrant.getQuadrantEndX() -  mLatitudeTitlePadding
							- mPaintFont.measureText(mBottomTitles.get(1));
					
					float textStartX = Math.max(x, startX + mTopTitleOffset
							 + mPaintFont.measureText(mBottomTitles.get(0)));
					
					canvas.drawText(mBottomTitles.get(1), textStartX, endY, mPaintFont);
				}
			}
		}
	}
	
	@Override
	public void onTranslation(float translationX) {
		this.mTranslationX = translationX;
		postInvalidate();
	}
	
	@Override
	public void onTranslation(float translationX, float totalTranslationX) {
		this.mTranslationX = translationX;
		this.mTotalTranslationX = totalTranslationX;
		postInvalidate();
	}

	/**
	 * @param stickData
	 */
	public void setStickData(IChartData<IStickEntity> stickData) {
		this.mStickData = stickData;
		if (mStickData != null) {
			if (mStickData.size() >= mDisplayNumber) {
				mDisplayNumberFrom = mStickData.size() - mDisplayNumber;
			} else {
				mDisplayNumberFrom = 0;
			}
			if (mDisplayNumberBegin == -1) {
				mDisplayNumberBegin = mDisplayNumberFrom;
			}
		}
	}
	
	/**
	 * @param linesData
	 */
	public void setLinesData(List<LineEntity<DateValueEntity>> linesData) {
		this.mLinesData = linesData;
	}
	
	/**
	 * @param displayNumber
	 */
	public void setDisplayNumber(int displayNumber) {
		this.mDisplayNumber = displayNumber;
	}
	
	/**
	 * @param topTitleFormat
	 */
	public void setTopTitleFormat(String topTitleFormat) {
		this.mTopTitleFormat = topTitleFormat;
	}

	/**
	 * @param stickSpacing
	 */
	public void setStickSpacing(int stickSpacing) {
		this.mStickSpacing = stickSpacing;
	}

	/**
	 * @param mPositiveStickBorderColor
	 */
	public void setPositiveStickBorderColor(int mPositiveStickBorderColor) {
		this.mPositiveStickBorderColor = mPositiveStickBorderColor;
	}
	
	/**
	 * @param positiveStickFillColor
	 */
	public void setPositiveStickFillColor(int positiveStickFillColor) {
		this.mPositiveStickFillColor = positiveStickFillColor;
	}

	/**
	 * @param negativeStickBorderColor
	 */
	public void setNegativeStickBorderColor(int negativeStickBorderColor) {
		this.mNegativeStickBorderColor = negativeStickBorderColor;
	}

	/**
	 * @param negativeStickFillColor
	 */
	public void setNegativeStickFillColor(int negativeStickFillColor) {
		this.mNegativeStickFillColor = negativeStickFillColor;
	}

	/**
	 * @param listener
	 */
	public void setOnTranslationListener(OnTranslationListener listener) {
		this.mTranslationListener = listener;
	}
	
}
