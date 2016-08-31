package com.gxq.tpm.ui.chart;

import com.letcome.R;
import com.gxq.tpm.tools.NumberFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbstractBaseChart extends View implements IChart {

	public static final String LOG_TAG = "AbstractBaseChart";

	protected boolean mDisplayBorder = true;
	protected int mBorderColor = Color.BLACK;
	protected float mBorderWidth = 1f;
	
	public AbstractBaseChart(Context context) {
		super(context);
	}

	public AbstractBaseChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void initChart() {
		mBorderWidth = 0;
		mBorderColor = getColor(R.color.color_f0f0f0);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mDisplayBorder) {
			drawBorder(canvas);
		}
	}

	protected void drawBorder(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setColor(mBorderColor);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		
		// draw a rectangle
		canvas.drawRect(getChartStartX(), getChartStartY(), getChartStartX() + getChartWidth() - mBorderWidth,
				getChartStartY() + getChartHeight() - mBorderWidth, mPaint);
	}

	protected int getChartStartX() {
		return (int) mBorderWidth;
	}
	
	protected int getChartStartY() {
		return (int) mBorderWidth;
	}
	
	protected int getChartHeight() {
		return super.getHeight();
	}

	protected int getChartWidth() {
		return super.getWidth();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}
	
	/**
	 * @param resId
	 * @return String
	 */
	protected String getString(int resId) {
		return getResources().getString(resId);
	}
	
	/**
	 * @param resId
	 * @return color
	 */
	protected int getColor(int resId) {
		return getResources().getColor(resId);
	}
	
	/**
	 * @param resId
	 * @return dimension
	 */
	protected int getDimension(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}
	
	/**
	 * @param pattern
	 * @param value
	 * @return
	 */
	protected String numberFormat(String pattern, double value) {
		return NumberFormat.decimalFormat(pattern, value);
	}
	
	/**
	 * @param fm
	 * @param top
	 * @return
	 */
	protected float getStartYFromTop(FontMetrics fm, float top) {
		return top - fm.top;
	}

	/**
	 * @param fm
	 * @param center
	 * @return
	 */
	protected float getStartYFromCenter(FontMetrics fm, float center) {
		float textHeight = (fm.bottom - fm.top);
		return center + textHeight / 2 - fm.bottom;
	}

	/**
	 * @param fm
	 * @param bottom
	 * @return
	 */
	protected float getStartYFromBottom(FontMetrics fm, float bottom) {
		return bottom - fm.bottom;
	}

	/**
	 * @return the displayBorder
	 */
	public boolean isDisplayBorder() {
		return mDisplayBorder;
	}

	/**
	 * @param displayBorder
	 */
	public void setDisplayBorder(boolean displayBorder) {
		this.mDisplayBorder = displayBorder;
	}

	/**
	 * @param borderColor
	 */
	public void setBorderColor(int borderColor) {
		this.mBorderColor = borderColor;
	}

	/**
	 * @param borderWidth
	 */
	public void setBorderWidth(float borderWidth) {
		this.mBorderWidth = borderWidth;
	}
	
}
