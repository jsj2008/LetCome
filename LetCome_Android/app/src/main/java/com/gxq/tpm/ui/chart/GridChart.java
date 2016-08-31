package com.gxq.tpm.ui.chart;

import java.util.List;

import com.letcome.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;

public class GridChart extends AbstractBaseChart implements IGrid {

	public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
			new float[] { 6, 3, 6, 3 }, 1);

	protected String mLoading = "";
	protected int mLoadingColor = Color.BLACK;
	protected int mLoadingFontSize = 15;

	// 图表头
	protected List<ValueColor> mTopTitles;
	protected int mTopTitleOffset = 0; // 图表头部字符间隔
	protected float mTopTitleQuadrantHeight = DEFAULT_TITLE_QUADRANT_HEIGHT; // 图表头部高度

	// 图表下标 
	protected List<String> mBottomTitles;
	protected int[] mTitleRatio; // 图表坐标间隔比例
	protected float mBottomTitleQuadrantHeight = DEFAULT_TITLE_QUADRANT_HEIGHT; // 图表坐标高度

	protected int mTitleFontColor = DEFAULT_TITLE_FONT_COLOR; // 图表坐标颜色
	protected int mTitleFontSize = DEFAULT_TITLE_FONT_SIZE;	// // 图表坐标字符大小
	protected int mTitleBackgroundColor = Color.BLACK;
	
	// 经线
	protected int mLongitudeLineColor = Color.WHITE;
	protected float mLongitudeLineWidth = DEFAULT_LINE_WIDTH;
	
	// 纬线
	protected int mLatitudeLineColor =  Color.WHITE;
	protected float mLatitudeLineWidth = DEFAULT_LINE_WIDTH;
	
	// 纬线坐标
	protected List<ValueColor> mLatitudeLeftTitles;
	protected List<ValueColor> mLatitudeRightTitles;
	
	protected int mLatitudeTitleFontSize = DEFAULT_LATITUDE_FONT_SIZE; // 图表纵坐标字符大小
	protected int mLatitudeTitlePadding = 0; // 图表纵坐标字符距离边框距离
	
	protected boolean mDashLongitude = DEFAULT_DASH_LONGITUDE;
	protected boolean mDashLatitude = DEFAULT_DASH_LATITUDE;

	protected PathEffect mDashEffect = DEFAULT_DASH_EFFECT;

	protected IQuadrant mDataQuadrant = new Quadrant(this) {
		public float getQuadrantWidth() {
			return getWidth() - 2 * mBorderWidth;
		}

		public float getQuadrantHeight() {
			return getHeight() - mTopTitleQuadrantHeight 
					- mBottomTitleQuadrantHeight
					- 2 * mBorderWidth;
		}

		public float getQuadrantStartX() {
			return mBorderWidth;
		}

		public float getQuadrantStartY() {
			return mBorderWidth + mTopTitleQuadrantHeight;
		}
	};

	public GridChart(Context context) {
		super(context);
	}

	public GridChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void initChart() {
		super.initChart();

		mLoading = getString(R.string.chart_loading);
		mLoadingColor = getColor(R.color.chart_loading_color);
		mLoadingFontSize = getDimension(R.dimen.font_size_xhdpi_11);
		
		mTopTitleOffset = 0;
		mLongitudeLineWidth = mLatitudeLineWidth = getDimension(R.dimen.horizontal_line_separate_height);
		mLongitudeLineColor = mLatitudeLineColor = getColor(R.color.color_f0f0f0);
		mTitleBackgroundColor = getColor(R.color.color_f8f8f8);
		
		// 左右距离边框的距离
		mLatitudeTitlePadding = getDimension(R.dimen.margin_xhdpi_6);
		
		mTitleFontSize = getDimension(R.dimen.font_size_xhdpi_7);
		mTitleFontColor = getColor(R.color.text_color_sub);
		
		mLatitudeTitleFontSize = getDimension(R.dimen.font_size_xhdpi_7);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//draw wait text
		if (!hasDrawData()) {
			drawLoading(canvas);
			return;
		}
		
		drawTopTitle(canvas); 
		drawLongitudeLine(canvas); // 画经线
		drawBottomTitle(canvas);
	
		drawLatitudeLine(canvas);// 画玮线
		drawLatitudeTitle(canvas);
	}
	
	protected boolean hasDrawData() {
		return false;
	}
	
	protected void drawLoading(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setColor(mLoadingColor);
		mPaint.setTextSize(mLoadingFontSize);
		mPaint.setAntiAlias(true);
		
		FontMetrics fontMetrics = mPaint.getFontMetrics();
		canvas.drawText(mLoading, mDataQuadrant.getQuadrantPaddingStartX()
					+ (mDataQuadrant.getQuadrantPaddingWidth() - mPaint.measureText(mLoading)) / 2,
				getStartYFromCenter(fontMetrics, mDataQuadrant.getQuadrantPaddingStartY() + 
						mDataQuadrant.getQuadrantPaddingHeight() / 2), mPaint);
	}
	
	protected void drawTopTitle(Canvas canvas) {
		if (mTopTitleQuadrantHeight <= 0 ||
				null == mTopTitles || mTopTitles.size() < 1) {
			return;
		}
		
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(mTitleBackgroundColor);
		canvas.drawRect(mDataQuadrant.getQuadrantStartX(), mDataQuadrant.getQuadrantStartY(), 
				mDataQuadrant.getQuadrantEndX(), mDataQuadrant.getQuadrantStartY() - mTopTitleQuadrantHeight, paint);
		
		Paint mPaintFont = new Paint();
		mPaintFont.setColor(mTitleFontColor);
		mPaintFont.setTextSize(mTitleFontSize);
		mPaintFont.setAntiAlias(true);
		
		FontMetrics fm = mPaintFont.getFontMetrics();
		
		float startX = mDataQuadrant.getQuadrantPaddingStartX() + mLatitudeTitlePadding;
		float startY = getStartYFromCenter(fm, mTopTitleQuadrantHeight / 2);
		
		for (int i = 0; i < mTopTitles.size(); i++) {
			ValueColor title = mTopTitles.get(i);
			mPaintFont.setColor(title.getColor());
			canvas.drawText(title.getValue(), startX, startY, mPaintFont);
			
			startX += mPaintFont.measureText(title.getValue()) + mTopTitleOffset;
		}
	}
	
	protected void drawLongitudeLine(Canvas canvas) {
		if (mTitleRatio == null || mTitleRatio.length < 1) {
			return;
		}
		
		Paint mPaintLine = new Paint();
		mPaintLine.setStyle(Style.STROKE);
		mPaintLine.setColor(mLongitudeLineColor);
		mPaintLine.setStrokeWidth(mLongitudeLineWidth);
		mPaintLine.setAntiAlias(true);

		if (mDashLongitude) {
			mPaintLine.setPathEffect(mDashEffect);
		}
		
		float startX = mDataQuadrant.getQuadrantPaddingStartX();
		int counts = mTitleRatio.length;
		for (int i = 1; i < counts; i++) {
			Path path = new Path();
			path.moveTo(startX + ratioLongitudePostOffset(i), mDataQuadrant.getQuadrantStartY());
			path.lineTo(startX + ratioLongitudePostOffset(i), mDataQuadrant.getQuadrantEndY());
			canvas.drawPath(path, mPaintLine);
		}
	}
	
	protected void drawBottomTitle(Canvas canvas) {
		if (mBottomTitleQuadrantHeight <= 0 || null == mBottomTitles || mBottomTitles.size() < 2) {
			return;
		}

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(mTitleBackgroundColor);
		canvas.drawRect(mDataQuadrant.getQuadrantStartX(), mDataQuadrant.getQuadrantEndY(), 
				mDataQuadrant.getQuadrantEndX(), mDataQuadrant.getQuadrantEndY() + mBottomTitleQuadrantHeight, paint);
		
		Paint mPaintFont = new Paint();
		mPaintFont.setColor(mTitleFontColor);
		mPaintFont.setTextSize(mTitleFontSize);
		mPaintFont.setAntiAlias(true);

		FontMetrics fontMetrics = mPaintFont.getFontMetrics();
		float postOffset = mDataQuadrant.getQuadrantPaddingWidth() / (mBottomTitles.size() - 1);
		float startX = mDataQuadrant.getQuadrantPaddingStartX();
		float startY = getStartYFromCenter(fontMetrics, mDataQuadrant.getQuadrantEndY() + mBottomTitleQuadrantHeight/2);

		for (int i = 0; i < mBottomTitles.size(); i++) {
			if (0 == i) {
				canvas.drawText(mBottomTitles.get(i), startX + mLatitudeTitlePadding, startY, mPaintFont);
			} else if (i == mBottomTitles.size() - 1) {
				float x = mDataQuadrant.getQuadrantPaddingWidth() - mPaintFont.measureText(mBottomTitles.get(i));
				canvas.drawText(mBottomTitles.get(i), x, startY, mPaintFont);
			} else {
				float x = 0;
				if (mTitleRatio != null) {
					x = startX + ratioLongitudePostOffset(i)
							- mPaintFont.measureText(mBottomTitles.get(i)) / 2f;
				} else {
					x = startX + i * postOffset
							- mPaintFont.measureText(mBottomTitles.get(i)) / 2f;
				}
				canvas.drawText(mBottomTitles.get(i), x, startY, mPaintFont);
			}
		}
	}
	
	protected float ratioLongitudePostOffset(int index) {
		int length = 0;
		int post = 0;
		for (int i = 0; i < mTitleRatio.length; i++) {
			length += mTitleRatio[i];
			if (i < index) {
				post += mTitleRatio[i];
			}
		}
		return mDataQuadrant.getQuadrantPaddingWidth() * post / length;
	}
	
	protected void drawLatitudeLine(Canvas canvas) {
		int latitudeTitlesCount = 0;
		if (null == mLatitudeLeftTitles || mLatitudeLeftTitles.size() <= 2) { 
			if (null == mLatitudeRightTitles || mLatitudeRightTitles.size() <= 2) {
				return;
			} else {
				latitudeTitlesCount = mLatitudeRightTitles.size();	
			}
		} else {
			latitudeTitlesCount = mLatitudeLeftTitles.size();
		}
		
		float width = mDataQuadrant.getQuadrantWidth();

		Paint mPaintLine = new Paint();
		mPaintLine.setStyle(Style.STROKE);
		mPaintLine.setColor(mLatitudeLineColor);
		mPaintLine.setStrokeWidth(mLatitudeLineWidth);
		mPaintLine.setAntiAlias(true);

		if (mDashLatitude) {
			mPaintLine.setPathEffect(mDashEffect);
		}

		float postOffset = this.mDataQuadrant.getQuadrantPaddingHeight()
				/ (latitudeTitlesCount - 1);
		float offset = mDataQuadrant.getQuadrantPaddingEndY();

		float startFrom = mDataQuadrant.getQuadrantStartX();
		
		for (int i = 1; i < latitudeTitlesCount - 1; i++) {
			Path path = new Path();
			path.moveTo(startFrom, offset - i * postOffset);
			path.lineTo(startFrom + width, offset - i * postOffset);
			canvas.drawPath(path, mPaintLine);
		}
	}
	
	protected void drawLatitudeTitle(Canvas canvas) {
		if (null == mLatitudeLeftTitles && null == mLatitudeRightTitles) {
			return;
		}

		if (mLatitudeLeftTitles != null && mLatitudeLeftTitles.size() > 0) {
			drawLatitudeLeftTitle(canvas); 
		}
		
		if (mLatitudeRightTitles != null && mLatitudeRightTitles.size() > 0) {
			drawLatitudeRightTitle(canvas);
		}
	}
	
	protected void drawLatitudeLeftTitle(Canvas canvas) {
		if (null != mLatitudeLeftTitles && mLatitudeLeftTitles.size() > 1) {
			Paint mPaintFont = new Paint();
			mPaintFont.setTextSize(mLatitudeTitleFontSize);
			mPaintFont.setAntiAlias(true);
			
			FontMetrics fontMetrics = mPaintFont.getFontMetrics();
			float endY = mDataQuadrant.getQuadrantPaddingEndY();
			
			float startX = mDataQuadrant.getQuadrantPaddingStartX() + mLatitudeTitlePadding;
			int titlesSize = mLatitudeLeftTitles.size();
			
			float postOffset = mDataQuadrant.getQuadrantPaddingHeight() / (mLatitudeLeftTitles.size() - 1);
			mPaintFont.setTextAlign(Align.LEFT);
			
			for (int i = 0; i < titlesSize; i++) {
				ValueColor valueColor = mLatitudeLeftTitles.get(i);
				if (valueColor.getValue() != null && valueColor.getValue().length() > 0) {
					mPaintFont.setColor(valueColor.getColor());
					if (0 == i) {
						canvas.drawText(valueColor.getValue(), startX, 
								getStartYFromBottom(fontMetrics, mDataQuadrant.getQuadrantPaddingEndY()) - mLatitudeTitlePadding,
								mPaintFont);
					} else if(titlesSize == i + 1) {
						canvas.drawText(valueColor.getValue(), startX,
								getStartYFromTop(fontMetrics, mDataQuadrant.getQuadrantPaddingStartY()) + mLatitudeTitlePadding,
								mPaintFont);
					} else {
						canvas.drawText(valueColor.getValue(), startX,
								getStartYFromCenter(fontMetrics, endY - i * postOffset),
								mPaintFont);
					}
				}
			}
		}
	}
	
	protected void drawLatitudeRightTitle(Canvas canvas) {
		if (null != mLatitudeRightTitles && mLatitudeRightTitles.size() > 1) {
			Paint mPaintFont = new Paint();
			mPaintFont.setTextSize(mLatitudeTitleFontSize);
			mPaintFont.setAntiAlias(true);
			
			FontMetrics fontMetrics = mPaintFont.getFontMetrics();
			float endY = mDataQuadrant.getQuadrantPaddingEndY();
			
			float startX = mDataQuadrant.getQuadrantPaddingEndX() - mLatitudeTitlePadding;
			int titlesSize = mLatitudeRightTitles.size();
			
			float postOffset = mDataQuadrant.getQuadrantPaddingHeight() / (mLatitudeRightTitles.size() - 1);
			
			mPaintFont.setTextAlign(Align.RIGHT);
			
			for (int i = 0; i < titlesSize; i++) {
				ValueColor valueColor = mLatitudeRightTitles.get(i);
				mPaintFont.setColor(valueColor.getColor());
				if (0 == i) {
					canvas.drawText(valueColor.getValue(), startX,
							getStartYFromBottom(fontMetrics, mDataQuadrant.getQuadrantEndY()) - mLatitudeTitlePadding,
							mPaintFont);
				} else if(titlesSize == i+1) {
					canvas.drawText(valueColor.getValue(), startX,
							getStartYFromTop(fontMetrics, mDataQuadrant.getQuadrantStartY()) + mLatitudeTitlePadding,
							mPaintFont);
				} else {
					canvas.drawText(valueColor.getValue(), startX,
							getStartYFromCenter(fontMetrics, endY - i * postOffset),
							mPaintFont);
				}
			}
		}
	}
	
	@Override
	protected int getChartStartY() {
		return (int) (super.getChartStartY() + mTopTitleQuadrantHeight);
	}
	
	@Override
	protected int getChartHeight() {
		return (int) (super.getChartHeight() - mBottomTitleQuadrantHeight - mTopTitleQuadrantHeight);
	}
	
	/**
	 * @param longitudeTitles
	 */
	public void setTopTitles(List<ValueColor> topTitles) {
		this.mTopTitles = topTitles;
	}

	/**
	 * @param topTitleOffset
	 */
	public void setTopTitleOffset(int topTitleOffset) {
		this.mTopTitleOffset = topTitleOffset;
	}

	/**
	 * @param topTitleQuadrantHeight
	 */
	public void setTopTitleQuadrantHeight(float topTitleQuadrantHeight) {
		this.mTopTitleQuadrantHeight = topTitleQuadrantHeight;
	}

	/**
	 * @param bottomTitles
	 */
	public void setBottomTitles(List<String> bottomTitles) {
		this.mBottomTitles = bottomTitles;
	}

	/**
	 * @param titleRatio
	 */
	public void setBottomTitles(int[] titleRatio) {
		this.mTitleRatio = titleRatio;
	}

	/**
	 * @param bottomTitles
	 * @param titleRatio
	 */
	public void setBottomTitles(List<String> bottomTitles, int[] titleRatio) {
		this.mBottomTitles = bottomTitles;
		this.mTitleRatio = titleRatio;
	}
	
	/**
	 * @param bottomTitleQuadrantHeight
	 */
	public void setBottomTitleQuadrantHeight(float bottomTitleQuadrantHeight) {
		this.mBottomTitleQuadrantHeight = bottomTitleQuadrantHeight;
	}

	/**
	 * @param titleFontColor
	 */
	public void setTitleFontColor(int titleFontColor) {
		this.mTitleFontColor = titleFontColor;
	}

	/**
	 * @param titleFontSize
	 */
	public void setTitleFontSize(int titleFontSize) {
		this.mTitleFontSize = titleFontSize;
	}
	
	/**
	 * @param titleBackgroundColor
	 */
	public void setTitleBackgroundColor(int titleBackgroundColor) {
		this.mTitleBackgroundColor = titleBackgroundColor;
	}
	

	/**
	 * @param longitudeColor
	 */
	public void setLongitudeLineColor(int longitudeLineColor) {
		this.mLongitudeLineColor = longitudeLineColor;
	}

	/**
	 * @param longitudeLineWidth
	 */
	public void setLongitudeWidth(float longitudeLineWidth) {
		this.mLongitudeLineWidth = longitudeLineWidth;
	}

	/**
	 * @param latitudeLineColor
	 */
	public void setLatitudeColor(int latitudeLineColor) {
		this.mLatitudeLineColor = latitudeLineColor;
	}
	
	/**
	 * @param latitudeLineWidth
	 */
	public void setLatitudeWidth(float latitudeLineWidth) {
		this.mLatitudeLineWidth = latitudeLineWidth;
	}
	
	/**
	 * @param latitudeTitles
	 */
	public void setLatitudeLeftTitles(List<ValueColor> latitudeTitles) {
		this.mLatitudeLeftTitles = latitudeTitles;
	}
	
	/**
	 * @param latitudeTitles
	 */
	public void setLatitudeRightTitles(List<ValueColor> latitudeTitles) {
		this.mLatitudeRightTitles = latitudeTitles;
	}
	
	/**
	 * @param latitudeTitleFontSize
	 */
	public void setLatitudeTitleFontSize(int latitudeTitleFontSize) {
		this.mLatitudeTitleFontSize = latitudeTitleFontSize;
	}

	/**
	 * @param latitudeTitlePadding
	 */
	public void setLatitudeTitlePadding(int latitudeTitlePadding) {
		this.mLatitudeTitlePadding = latitudeTitlePadding;
	}

	/**
	 * @param dashLongitude
	 */
	public void setDashLongitude(boolean dashLongitude) {
		this.mDashLongitude = dashLongitude;
	}

	/**
	 * @param dashLatitude
	 */
	public void setDashLatitude(boolean dashLatitude) {
		this.mDashLatitude = dashLatitude;
	}

	/**
	 * @param dashEffect
	 */
	public void setDashEffect(PathEffect dashEffect) {
		this.mDashEffect = dashEffect;
	}

}
