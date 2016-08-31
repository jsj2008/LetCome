package com.gxq.tpm.ui.chart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.letcome.R;

public class ScrollableVolumeStickChart extends ScrollableStickChart {
	
	protected int mTitleColor;

	public ScrollableVolumeStickChart(Context context) {
		super(context);
	}

	public ScrollableVolumeStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initChart() {
		super.initChart();
		
		mBottomTitleQuadrantHeight = getDimension(R.dimen.margin_xhdpi_15);
		
		mTitleColor = getColor(R.color.text_color_sub);
		mTopTitleFormat = "0";
	}
	
	@Override
	protected void calcStickDataValueRange() {
		super.calcStickDataValueRange();
		this.mMinValue = 0;
		
		if (mStickData.hasData()) {
			List<ValueColor> latitudeTitles = new ArrayList<ValueColor>(); 
			
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, mMinValue), mTitleColor));
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, mMaxValue), mTitleColor));
			
			setLatitudeLeftTitles(latitudeTitles);
		}
	}
	
	@Override
	protected void calcTopTitle() {
		if (mLinesData != null) {
			List<ValueColor> topTitleList = new ArrayList<ValueColor>();
			if (mStickData != null && mStickData.size() > 0) {
				VolumeEntity entity = (VolumeEntity) mStickData.get(mStickData.size() - 1);
				topTitleList.add(new ValueColor("Vol:" + numberFormat(mTopTitleFormat, entity.getVolume()),
						mTitleColor));
			}
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
	
	@Override
	protected void drawSticks(Canvas canvas) {
		if (mStickData.hasData()) {
			canvas.save();
			canvas.clipRect(mDataQuadrant.getQuadrantPaddingStartX(), mDataQuadrant.getQuadrantPaddingStartY(), 
					mDataQuadrant.getQuadrantPaddingEndX(), mDataQuadrant.getQuadrantPaddingEndY());
			
			Paint mPaintPositive = new Paint();
			mPaintPositive.setColor(mPositiveStickFillColor);

			Paint mPaintNegative = new Paint();
			mPaintNegative.setColor(mNegativeStickFillColor);
			float stickWidth = mDataQuadrant.getQuadrantPaddingWidth() / mDisplayNumber;
			float startX = 0;
			
			int size = mStickData.size(); 
			if (size > mDisplayNumber) {
				float transtralteX = (mTotalTranslationX + mTranslationX) < 0 ? 0 : (mTotalTranslationX + mTranslationX);
				startX = (mDisplayNumber - size) * stickWidth + transtralteX;
				if (startX > 0) {
					startX = 0;
					mTotalTranslationX = (size - mDisplayNumber) * stickWidth - mTranslationX;
				}
			}
			
			float maxY = (float) (mDataQuadrant.getQuadrantPaddingHeight() + mDataQuadrant.getQuadrantPaddingStartY());
			for (int i = 0; i < size; i++) {
				VolumeEntity volumeEntity = (VolumeEntity) mStickData.get(i);
				float volumeY = (float) (getChartYFromValue(volumeEntity.getVolume()));
	
				if (volumeEntity.getOpen() <= volumeEntity.getClose()) {
					canvas.drawRect(startX + mStickSpacing, volumeY, startX + stickWidth - mStickSpacing,
							maxY, mPaintPositive);
				} else if (volumeEntity.getOpen() > volumeEntity.getClose()) {
					canvas.drawRect(startX + mStickSpacing, volumeY, startX + stickWidth - mStickSpacing,
							maxY, mPaintNegative);
				}
	
				// next x
				startX = startX + stickWidth;
			}
			
			if (mLinesData != null && mLinesData.size() > 0) {
				float lineEndX = startX - stickWidth / 2;
				for (LineEntity<DateValueEntity> dataValueEntity : mLinesData) {
					Paint linePaint = new Paint();
					linePaint.setAntiAlias(true); 
					linePaint.setStyle(Paint.Style.STROKE); 
					linePaint.setColor(dataValueEntity.getLineColor());
					Path linePath = new Path();
					
					List<DateValueEntity> entity = dataValueEntity.getLineData();
					int entitySize = entity.size();
					if (entitySize > 1) {
						linePath.moveTo(lineEndX, getChartYFromValue(entity.get(entitySize - 1).getValue()));
						
						for (int i = entitySize - 2; i > 0; i--) {
							linePath.lineTo(lineEndX - stickWidth * (entitySize - i),
									getChartYFromValue(entity.get(i).getValue()));
						}
					}
					canvas.drawPath(linePath, linePaint);
				}
			}
			
			canvas.restore();
		}
	}
	
	/**
	 * @param titleColor
	 */
	public void setTitleColor(int titleColor) {
		this.mTitleColor = titleColor;
	}

}
