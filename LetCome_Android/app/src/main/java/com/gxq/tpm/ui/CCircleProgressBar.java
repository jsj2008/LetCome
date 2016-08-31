package com.gxq.tpm.ui;

import com.letcome.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CCircleProgressBar extends View {
	private float mTotal, mProgress;
	private Paint mTotalPaint, mProgressPaint;
	private int mStrokeWidth;
	
	private RectF mRect = new RectF();

	public CCircleProgressBar(Context context) {
		this(context, null);
	}

	public CCircleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CCircleProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_6);
		mTotalPaint = new Paint();
		mTotalPaint.setAntiAlias(true);
		mTotalPaint.setStrokeWidth(mStrokeWidth);
		mTotalPaint.setColor(getResources().getColor(R.color.translucent_white_color));
		mTotalPaint.setStyle(Style.STROKE);
		mTotalPaint.setStrokeCap(Cap.ROUND);
		
		mProgressPaint = new Paint(mTotalPaint);
		mProgressPaint.setStrokeWidth(mStrokeWidth);
		mProgressPaint.setColor(getResources().getColor(R.color.white_color));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		
		int raduis = Math.min(height - mStrokeWidth * 2, width - mStrokeWidth * 2) / 2;
		
		int x = width/2;
		int y = height/2;
		
		mRect.set(x - raduis, y - raduis, x + raduis, y + raduis); 
		
		if (mTotal > 0) {
			if (mProgress < 0) {
				canvas.drawArc(mRect, 0, 360, false, mTotalPaint);
			} else if (mProgress > mTotal) {
				canvas.drawArc(mRect, 0, 360, false, mProgressPaint);
			} else {
				int swapAngle = (int) (mProgress * 360 / mTotal);
		
				canvas.drawArc(mRect, (270 + swapAngle) % 360, 360 - swapAngle, false, mTotalPaint);
		
				if (swapAngle != 0) {
					canvas.drawArc(mRect, 270, swapAngle, false, mProgressPaint);
				}
			}
		} else {
			canvas.drawArc(mRect, 0, 360, false, mTotalPaint);
		}
	}
	
	public void setStrokeWidth(int size) {
		mStrokeWidth = size;
		mTotalPaint.setStrokeWidth(mStrokeWidth);
		mProgressPaint.setStrokeWidth(mStrokeWidth);
	}
	
	public void setTotal(float total) {
		if (total != mTotal) {
			this.mTotal = total;
			postInvalidate();
		}
	}
	public void setProgress(float progress) {
		setProgress(progress, mTotal);
	}
	
	public void setProgress(float progress, float total) {
		if (progress != mProgress || total != mTotal) {
			this.mProgress = progress;
			this.mTotal = total;
			
			postInvalidate();
		}
	}

}

