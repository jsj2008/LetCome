package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.letcome.R;
import com.gxq.tpm.tools.BitmapUtil;
//import com.gxq.a50.prefs.LockPrefs;
//import com.weibopay.android.app.encrypt.PasswordEncrypt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LockPatternView extends View {
	private static final int MIN_POINT_NUM		= 4;
	private static final int ERROR_LAST_TIME	= 300;
	
	private Bitmap mLocalCircleOriginal;
	private Bitmap mLocalCircleClick;
	private Bitmap mLocalCircleClickError;
	private Bitmap mLocalLine;
	private Bitmap mLocalLineError;

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Point[][] mPoints = new Point[3][3];
	private List<Point> mSelectedPoints = new ArrayList<Point>();

	private float mRadius = 0;

	private boolean mFinish = false;
	public boolean isSet = true;

	private boolean isTouch = true;
	private boolean mInitialized = false;
	private boolean mPointOutside = false;
	private OnCompleteListener mCompleteListener;

	private float moveX, moveY;
	private String mCurrentPwd;
	
	private int mMinPointNum = MIN_POINT_NUM;
	
	public LockPatternView(Context context) {
		this(context, null);
	}

	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected void onDraw(Canvas canvas) {
		if(isInEditMode())
			return;
		if (!mInitialized)
			init();
		drawCanvas(canvas);
	}

	private void init() {
		// 鍔犺浇鍥剧墖
		mLocalCircleOriginal = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_circle_normal);
		mLocalCircleClick = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_circle_click);
		mLocalCircleClickError = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_circle_error);
		mLocalLine = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_line);
		mLocalLineError = BitmapFactory.decodeResource(getResources(),
				R.drawable.gesture_line_error);

		int width = this.getWidth();
		int height = this.getHeight();

		float x = 0;
		float y = 0;

		float canvasMinWidth = width;

		if (width > height) {
			x = (width - height) / 2;
			width = height;
			canvasMinWidth = height;
		}
		if (width < height) {
			y = (height - width) / 2;
			height = width;
		}

		float circleMinWidth = canvasMinWidth / 6.0f;

		if (mLocalCircleOriginal.getWidth() > circleMinWidth) {
			float scale = circleMinWidth * 1.2f
					/ mLocalCircleOriginal.getWidth();
			mLocalCircleOriginal = BitmapUtil.zoom(mLocalCircleOriginal, scale);
			mLocalCircleClick = BitmapUtil.zoom(mLocalCircleClick, scale);
			mLocalCircleClickError = BitmapUtil.zoom(mLocalCircleClickError, scale);
			mLocalLine = BitmapUtil.zoom(mLocalLine, scale);
			mLocalLineError = BitmapUtil.zoom(mLocalLineError, scale);
		}

		mRadius = mLocalCircleOriginal.getHeight() / 2;

		mPoints[0][0] = new Point(x + circleMinWidth, y + circleMinWidth, 0);
		mPoints[0][1] = new Point(x + width / 2, y + circleMinWidth, 1);
		mPoints[0][2] = new Point(x + width - circleMinWidth, y
				+ circleMinWidth, 2);
		mPoints[1][0] = new Point(x + circleMinWidth, y + height / 2, 3);
		mPoints[1][1] = new Point(x + width / 2, y + height / 2, 4);
		mPoints[1][2] = new Point(x + width - circleMinWidth, y
				+ height / 2, 5);
		mPoints[2][0] = new Point(x + circleMinWidth, y + height
				- circleMinWidth, 6);
		mPoints[2][1] = new Point(x + width / 2, y + height
				- circleMinWidth, 7);
		mPoints[2][2] = new Point(x + width - circleMinWidth, y
				+ height - circleMinWidth, 8);

		mInitialized = true;
	}

	private void drawCanvas(Canvas canvas) {
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (p.state == Point.STATE_CHECK) {
					canvas.drawBitmap(mLocalCircleClick, p.x - mRadius, p.y - mRadius,
							mPaint);
				} else if (p.state == Point.STATE_CHECK_ERROR) {
					canvas.drawBitmap(mLocalCircleClickError, p.x - mRadius, p.y
							- mRadius, mPaint);
				} else {
					canvas.drawBitmap(mLocalCircleOriginal, p.x - mRadius, p.y
							- mRadius, mPaint);

				}
			}
		}

		if (mSelectedPoints.size() > 0) {
			int tempAlpha = mPaint.getAlpha();
			// mPaint.setAlpha(50);
			Point tp = mSelectedPoints.get(0);
			for (int i = 1; i < mSelectedPoints.size(); i++) {
				Point p = mSelectedPoints.get(i);
				drawLine(canvas, tp, p);
				tp = p;
			}
			if (mPointOutside) {
				drawLine(canvas, tp, new Point((int) moveX, (int) moveY));
			}
			mPaint.setAlpha(tempAlpha);
		}

	}

	private void drawLine(Canvas canvas, Point tp, Point p) {
		float pointDistance = (float) distance(tp.x, tp.y, p.x, p.y);
		float degrees = caculateDegree(tp, p);
		canvas.rotate(degrees, tp.x, tp.y);
		Matrix mMatrix = new Matrix();
		mMatrix.setScale(pointDistance / mLocalLine.getWidth(), 1);
		mMatrix.postTranslate(tp.x, tp.y - mLocalLine.getHeight() / 2);
		if (p.state == Point.STATE_CHECK_ERROR) {
			canvas.drawBitmap(mLocalLineError, mMatrix, mPaint);
		} else {
			canvas.drawBitmap(mLocalLine, mMatrix, mPaint);
		}

		canvas.rotate(-degrees, tp.x, tp.y);
	}

	private float caculateDegree(Point tp, Point p) {
		float beforeX = tp.x;
		float beforeY = tp.y;
		float afterX = p.x;
		float afterY = p.y;
		float x = beforeX - afterX;
		float y = beforeY - afterY;
		float de1 = (float) Math.toDegrees(Math.atan2(y, x));
		float degree = 180 + de1;
		return degree;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!isTouch)
			return false;
		
		mPointOutside = false;
		mFinish = false;
		float ex = event.getX();
		float ey = event.getY();

		Point p = selectPoint(ex, ey);;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// isTouch = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if (p == null) {
				mPointOutside = true;
				moveX = ex;
				moveY = ey;
			}
			break;
		case MotionEvent.ACTION_UP:
			mFinish = true;
			isTouch = false;
			break;
		}

		if (mFinish) {
			if (mSelectedPoints.size() > 0) {
				if (mSelectedPoints.size() < mMinPointNum) {
					mCompleteListener.onComplete(false);
				} else {
					mCurrentPwd = currentPassword();
					mCompleteListener.onComplete(true);
				}
			} else{
				reset();
			}
		} else {
			if (p != null) {
				if (mSelectedPoints.contains(p)) {
					mPointOutside = true;
					moveX = ex;
					moveY = ey;
				} else {
					p.state = Point.STATE_CHECK;
					addPoint(p);
				}
			}
		}

		this.postInvalidate();
		return true;
	}

	public void reset() {
		for (Point tp : mSelectedPoints)
			tp.state = Point.STATE_NORMAL;
		mSelectedPoints.clear();
		isTouch = true;
		mCurrentPwd = null;
	}

	public void checkError() {
		for (Point tp : mSelectedPoints)
			tp.state = Point.STATE_CHECK_ERROR;
		new Timer().schedule(new TimerTask() {
			public void run() {
				reset();
				postInvalidate();
			}
		}, ERROR_LAST_TIME);
		invalidate();
	}

	private void addPoint(Point p) {
		this.mSelectedPoints.add(p);
	}

	private Point selectPoint(float ex, float ey) {
		for (Point[] points : mPoints) {
			for (Point p : points) {
				if (distance(p.x, p.y, ex, ey) < mRadius)
					return p;
			}
		}
		return null;
	}

	private double distance(float x, float y, float x1, float y1) {
		return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
	}

	private String currentPassword() {
		StringBuffer sf = new StringBuffer();
		for (Point p : mSelectedPoints) {
			sf.append(" ");
			sf.append(p.index);
		}
		return sf.toString().trim();
	}

	public String getPassword() {
		return mCurrentPwd;
	}

	public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
		this.mCompleteListener = mCompleteListener;
	}

	public interface OnCompleteListener {
		
		public void onComplete(boolean success);
	
	}
	
	private static class Point {
		public static int STATE_NORMAL = 0;
		public static int STATE_CHECK = 1;
		public static int STATE_CHECK_ERROR = 2;
		
		public float x, y;
		public int state = STATE_NORMAL;
		public int index = -1;

		public Point(float x, float y) {
			this(x, y, -1);
		}
		
		public Point(float x, float y, int index) {
			this.x = x;
			this.y = y;
			this.index = index;
		}

	}


}
