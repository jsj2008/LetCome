package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.NumberFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

public class CLossRatioView extends View {
	private float mPercent;
	private int mMax = 30;
	private int mProgress = 8;
	private int mSecondProgress = 15;
	
	private Bitmap mBitmap1, mBitmap2, mBitmap3;
	private Paint mPercentPaint;
	private Paint mProgressPaint;

	public CLossRatioView(Context context) {
		this(context, null);
	}

	public CLossRatioView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CLossRatioView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outHeight = context.getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_17);
		options.outWidth = context.getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_30);
		
		mBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.item_slider1, options);
		mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.item_slider2, options);
		mBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.item_slider3, options);
		
		mPercentPaint = new Paint();
		mPercentPaint.setColor(getResources().getColor(R.color.white_color));
		mPercentPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_size_xhdpi_9));
		mPercentPaint.setAntiAlias(true);
		
		mProgressPaint = new Paint();
		mProgressPaint.setColor(getResources().getColor(R.color.text_color_sub));
		mProgressPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_size_xhdpi_9));
		mProgressPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int startX = getDimension(R.dimen.margin_xhdpi_40);
		int endX = width - startX;

		if (mPercent >= 0) {
			int percentX = (int) (startX + (endX - startX) * mPercent / mMax);
			if (percentX > endX) percentX = endX;
			else if (percentX < startX) percentX = startX;
			drawPercent(canvas, percentX);
		}
		
		int progressX = startX + (endX - startX) * mProgress / mMax;
		int secondProgressX = startX + (endX - startX) * mSecondProgress / mMax;
		
		drawProgress(canvas, startX, progressX, secondProgressX, endX);
		
		drawProgressText(canvas, progressX, secondProgressX, endX);
	}
	
	private void drawPercent(Canvas canvas, int percentX) {
		Bitmap bitmap = null;
		if (mPercent <= mProgress) {
			bitmap = mBitmap1;
		} else if (mPercent <= mSecondProgress) {
			bitmap = mBitmap2;
		} else {
			bitmap = mBitmap3;
		}
		int bitmapWidth = bitmap.getWidth();
		canvas.drawBitmap(bitmap, percentX - bitmapWidth, 0, null);
		
		String text = NumberFormat.decimalFormat(mPercent) + "%";
		float startX = percentX - bitmapWidth / 2 - mPercentPaint.measureText(text) / 2;
		
		FontMetrics fm = mPercentPaint.getFontMetrics();
		float startY = getStartYFromTop(fm, 0);

		canvas.drawText(text, startX, startY, mPercentPaint);
	}
	
	private void drawProgress(Canvas canvas, int startX, int progressX, int secondProgressX, int endX) {
		int startY = getDimension(R.dimen.margin_xhdpi_18);
		int endY = getDimension(R.dimen.margin_xhdpi_22);
		int radius = getDimension(R.dimen.margin_xhdpi_2);
		
		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.color_4ebeff));
		RectF rect = new RectF(startX, startY, endX, endY);
		canvas.drawRoundRect(rect, radius, radius, paint);
		
		paint = new Paint();
		paint.setColor(getResources().getColor(R.color.color_ffaa00));
		rect = new RectF(startX, startY, secondProgressX, endY);
		canvas.drawRoundRect(rect, radius, radius, paint);
		
		paint = new Paint();
		paint.setColor(getResources().getColor(R.color.red_color));
		rect = new RectF(startX, startY, progressX, endY);
		canvas.drawRoundRect(rect, radius, radius, paint);
	}

	private void drawProgressText(Canvas canvas, int progressX, int secondProgressX, int endX) {
		int top = getDimension(R.dimen.margin_xhdpi_25);
		FontMetrics fm = mProgressPaint.getFontMetrics();
		float startY = getStartYFromTop(fm, top);
		
		String text = mProgress + "%";
		float startX = progressX - mProgressPaint.measureText(text) / 2;
		canvas.drawText(text, startX, startY, mProgressPaint);
		
		text = mSecondProgress + "%";
		startX = secondProgressX - mProgressPaint.measureText(text) / 2;
		canvas.drawText(text, startX, startY, mProgressPaint);
		
		text = mMax + "%+";
		startX = endX - mProgressPaint.measureText(text);
		canvas.drawText(text, startX, startY, mProgressPaint);
	}

	private int getDimension(int id) {
		return getResources().getDimensionPixelSize(id);
	}
	
	private float getStartYFromTop(FontMetrics fm, float top) {
		return top - fm.top;
	}
	
	public void setPercent(float percent) {
		this.mPercent = percent;
	}

	public void setProgress(int progress, int secondProgress, int max) {
		if (progress > 0)
			this.mProgress = progress;
		if (secondProgress > 0)
			this.mSecondProgress = secondProgress;
		if (max > 0)
			this.mMax = max;
	}
	
}
