package com.gxq.tpm.ui.chart;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;
import com.gxq.tpm.tools.NumberFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

public class ScrollableCandleStickChart extends ScrollableStickChart {
	private final static int LEFT 		= 1;
	private final static int CENTER 	= 2;
	private final static int RIGHT 		= 3;
	
	protected boolean mDisplayCurrentPrice;
	
	protected int mCurrentPriceFontSize;
	protected int mCurrentPriceColor;
	protected int mCurrentPriceFontColor;
	
	protected int mTouchPriceFontSize;
	protected int mTouchPriceColor;
	protected int mTouchPriceFontColor;
	
	protected int mPriceTopPadding;
	protected int mPriceBottomPadding;
	protected int mPriceLeftPadding;
	protected int mPriceRightPadding;
	
	protected float mCurPrice;
	
	public ScrollableCandleStickChart(Context context) {
		super(context);
	}

	public ScrollableCandleStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void initChart() {
		super.initChart();
		
		mDisplayCurrentPrice = true;
		
		mCurrentPriceFontSize = mTouchPriceFontSize = getDimension(R.dimen.font_size_xhdpi_9);
		mCurrentPriceColor = getColor(R.color.gain_color);
		mTouchPriceColor = getColor(R.color.color_9877DF);
		
		mCurrentPriceFontColor = mTouchPriceFontColor = getColor(R.color.white_color);
		
		mPriceTopPadding = mPriceBottomPadding = 4;
		mPriceLeftPadding = mPriceRightPadding = 8;
		
		setBottomTitles(new int[]{1, 1, 1, 1, 1});
	}
	
	@Override
	protected void calcStickDataValueRange() {
		super.calcStickDataValueRange();
		if (mStickData.hasData()) {
			List<ValueColor> latitudeTitles = new ArrayList<ValueColor>(); 
			int color = getColor(R.color.text_color_sub);
			double diff = mMaxValue - mMinValue;
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, mMinValue), color));
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, mMinValue + diff / 4), color));
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, (mMinValue + mMaxValue) / 2), color));
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, mMaxValue - diff / 4), color));
			latitudeTitles.add(new ValueColor(numberFormat(mTopTitleFormat, mMaxValue), color));
			
			setLatitudeLeftTitles(latitudeTitles);
			setLatitudeRightTitles(latitudeTitles);
		} else {
			setLatitudeRightTitles(null);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (hasDrawData() && mStickData.hasData()) {
			drawCurrentPrice(canvas);
			
			if (mRecord && mDownY > mDataQuadrant.getQuadrantPaddingStartY()
					&& mDownY < mDataQuadrant.getQuadrantPaddingEndY()) {
				double value = mMinValue + (mMaxValue - mMinValue) * 
						(mDataQuadrant.getQuadrantPaddingEndY() - mDownY) / mDataQuadrant.getQuadrantHeight();
				drawLine(canvas, value, mTouchPriceColor);
				float centerX = (mDataQuadrant.getQuadrantStartX() + mDataQuadrant.getQuadrantEndX()) / 2;
				int gravity = centerX >= mTouchX ? RIGHT : LEFT;
				drawPrice(canvas, value, mTouchPriceColor, mTouchPriceFontColor, mTouchPriceFontSize, gravity);
			}
		}
	}
	
	@Override
	protected void drawLatitudeLeftTitle(Canvas canvas) {
	}
	
	@Override
	protected void drawSticks(Canvas canvas) {
		if (!hasDrawData()) {
			return;
		}
		
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
			
			Log.i("Chart",  "startX = " + startX + " transtralteX = " + transtralteX);
		}
		for (int i = 0; i < size; i++) {
			OHLCEntity ohlc = (OHLCEntity) mStickData.get(i);
			float openY = getChartYFromValue(ohlc.getOpen()); 
			float highY = getChartYFromValue(ohlc.getHigh());
			float lowY = getChartYFromValue(ohlc.getLow());
			float closeY = getChartYFromValue(ohlc.getClose());

			if (ohlc.getOpen() < ohlc.getClose()) {
				// stick or line
				if (stickWidth >= 2f) {
					canvas.drawRect(startX + mStickSpacing, closeY, startX + stickWidth - mStickSpacing,
							openY, mPaintPositive);
				}
				canvas.drawLine(startX + stickWidth / 2f, highY, startX
						+ stickWidth / 2f, lowY, mPaintPositive);
			} else if (ohlc.getOpen() > ohlc.getClose()) {
				// stick or line
				if (stickWidth >= 2f) {
					canvas.drawRect(startX + mStickSpacing, openY, startX + stickWidth - mStickSpacing,
							closeY, mPaintNegative);
				}
				canvas.drawLine(startX + stickWidth / 2f, highY, startX
						+ stickWidth / 2f, lowY, mPaintNegative);
			} else {
				canvas.drawLine(startX + mStickSpacing, closeY, startX + stickWidth - mStickSpacing,
						openY, mPaintPositive);
				canvas.drawLine(startX + stickWidth / 2f, highY, startX
						+ stickWidth / 2f, lowY, mPaintPositive);
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
	
	private void drawCurrentPrice(Canvas canvas) {
		if (mDisplayCurrentPrice && mStickData.size() > 0) {
			OHLCEntity ohlc = (OHLCEntity) mStickData.get(mStickData.size() - 1);
			
			double curPrice = mCurPrice > 0 ? mCurPrice : ohlc.getClose();
			drawLine(canvas, curPrice,  mCurrentPriceColor);
			drawPrice(canvas, curPrice,  mCurrentPriceColor, mCurrentPriceFontColor, mCurrentPriceFontSize, CENTER);
		}
	}
	
	private void drawLine(Canvas canvas, double value, int color) {
		Paint linePaint = new Paint();
		linePaint.setColor(color);
		linePaint.setStyle(Style.STROKE);
		
		float centerY = getChartYFromValue(value);
		canvas.drawLine(mDataQuadrant.getQuadrantStartX(), centerY,
				mDataQuadrant.getQuadrantEndX(), centerY, linePaint);
	}
	
	private void drawPrice(Canvas canvas, double value, int color, int fontColor, int fontSize, int gravity) {
		String valueStr = NumberFormat.decimalFormat(value);
		
		Paint linePaint = new Paint();
		linePaint.setColor(color);
		linePaint.setStyle(Style.FILL);
		
		Paint pricePaint = new Paint();
		pricePaint.setColor(fontColor);
		pricePaint.setAntiAlias(true);
		pricePaint.setTextSize(fontSize);
		FontMetrics fm = pricePaint.getFontMetrics();
		
		float width = pricePaint.measureText(valueStr);
		float centerX = mDataQuadrant.getQuadrantStartX() + mDataQuadrant.getQuadrantWidth() / 2;
		float startX = centerX;
		if (gravity == LEFT) {
			startX = mDataQuadrant.getQuadrantStartX() + centerX / 2;
		} else if (gravity == RIGHT) {
			startX = mDataQuadrant.getQuadrantStartX() + centerX * 3 / 2;
		}
		
		float centerY = getChartYFromValue(value);
		float startY = getStartYFromCenter(fm, centerY);
		
		float priceStartY = centerY * 2 - startY - mPriceTopPadding;
		float priceEndY = startY + mPriceBottomPadding;
		float priceHight = priceEndY - priceStartY;
		
		if (priceStartY < mDataQuadrant.getQuadrantPaddingStartY()) {
			priceStartY = mDataQuadrant.getQuadrantPaddingStartY();
			priceEndY = priceStartY + priceHight;
			startY = priceEndY - mPriceBottomPadding;
		}
		if (priceEndY > mDataQuadrant.getQuadrantPaddingEndY()) {
			priceEndY = mDataQuadrant.getQuadrantPaddingEndY();
			priceStartY = priceEndY - priceHight;
			startY = priceEndY - mPriceBottomPadding;
		}
		
		canvas.drawRect(startX - width/2 - mPriceLeftPadding, priceStartY,
				startX + width/2 + mPriceRightPadding, priceEndY, linePaint);
		
		if (gravity == LEFT) {
			canvas.drawText(valueStr, centerX/2 - width/2, startY, pricePaint);
		} else if (gravity == CENTER) {
			canvas.drawText(valueStr, centerX - width/2, startY, pricePaint);
		} else if (gravity == RIGHT) {
			canvas.drawText(valueStr, centerX*3/2 - width/2, startY, pricePaint);
		}
	}
	
	/**
	 * @param displayCurrentPrice
	 */
	public void setDisplayCurrentPrice(boolean displayCurrentPrice) {
		this.mDisplayCurrentPrice = displayCurrentPrice;
	}

	/**
	 * @param currentPriceFontSize
	 */
	public void setCurrentPriceFontSize(int currentPriceFontSize) {
		this.mCurrentPriceFontSize = currentPriceFontSize;
	}

	/**
	 * @param currentPriceColor
	 */
	public void setCurrentPriceColor(int currentPriceColor) {
		this.mCurrentPriceColor = currentPriceColor;
	}
	
	/**
	 * @param currentPriceFontColor
	 */
	public void setCurrentPriceFontColor(int currentPriceFontColor) {
		this.mCurrentPriceFontColor = currentPriceFontColor;
	}

	/**
	 * @param touchPriceFontSize
	 */
	public void setTouchPriceFontSize(int touchPriceFontSize) {
		this.mTouchPriceFontSize = touchPriceFontSize;
	}

	/**
	 * @param touchPriceColor
	 */
	public void setTouchPriceColor(int touchPriceColor) {
		this.mTouchPriceColor = touchPriceColor;
	}

	/**
	 * @param touchPriceFontColor
	 */
	public void setTouchPriceFontColor(int touchPriceFontColor) {
		this.mTouchPriceFontColor = touchPriceFontColor;
	}


	/**
	 * @param priceTopPadding
	 */
	public void setPriceTopPadding(int priceTopPadding) {
		this.mPriceTopPadding = priceTopPadding;
	}

	/**
	 * @param priceBottomPadding
	 */
	public void setPriceBottomPadding(int priceBottomPadding) {
		this.mPriceBottomPadding = priceBottomPadding;
	}

	/**
	 * @param priceLeftPadding
	 */
	public void setPriceLeftPadding(int priceLeftPadding) {
		this.mPriceLeftPadding = priceLeftPadding;
	}

	/**
	 * @param priceRightPadding
	 */
	public void setPriceRightPadding(int priceRightPadding) {
		this.mPriceRightPadding = priceRightPadding;
	}
	
	/**
	 * @param curPrice
	 */
	public void setCurPrice(float curPrice) {
		this.mCurPrice = curPrice;
	}
	
}
