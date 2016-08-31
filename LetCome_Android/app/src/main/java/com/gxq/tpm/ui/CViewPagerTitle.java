package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.PageChangeListener;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CViewPagerTitle extends LinearLayout {

	private String[] mTitles;
	private TextView[] mTvTitles;
	private ImageView[] mIvTitles;
	private int mTitleCount;
	private int mTitleWidth;
	private int mPosition;
	
	private int mTranslationX;
	private int mTitleBarHeight;
	private Paint mPaint = new Paint();
	
	private ViewPager mViewPager;
	private OnTitleSelectionListener mTitleSelectionListener;
	
	private OnPageChangeListener mPageChangeListener = new PageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setSelect(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			scroll(position, positionOffset);
		}
	};
	
	public CViewPagerTitle(Context context) {
		this(context, null);
	}

	public CViewPagerTitle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CViewPagerTitle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setOrientation(HORIZONTAL);
		mPaint.setColor(Util.transformColor(R.color.tab_cur_selected));
		mTitleBarHeight = Util.transformDimen(R.dimen.margin_xhdpi_2);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mTitleWidth = w / mTitleCount;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		
		canvas.save();
		canvas.translate(mTranslationX, getHeight() - mTitleBarHeight);
		canvas.drawRect(getTitleStartX(), 0, getTitleEndX(), mTitleBarHeight, mPaint);
		canvas.restore();
	}
	
	protected int getTitleStartX() {
		return mTitleWidth / 6;
	}
	
	protected int getTitleEndX() {
		return mTitleWidth * 5 / 6;
	}
	
	public void setTitle(String[] titles) {
		this.mTitles = titles;
		this.mTvTitles = new TextView[titles.length];
		this.mIvTitles = new ImageView[titles.length];
		mTitleCount = mTitles.length;
		
		removeAllViews();
		
		setWeightSum(mTitleCount);
		for (int index = 0; index < mTitleCount; index++) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			View view = (View) inflater.inflate(R.layout.tab_title, this, false);
			mTvTitles[index] = (TextView) view.findViewById(R.id.tv_title);
			mIvTitles[index] = (ImageView) view.findViewById(R.id.iv_title);
			
			mTvTitles[index].setText(mTitles[index]);
			
			addView(view);
			
			final int position = index;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mTitleSelectionListener != null) {
						setSelect(position);
						mTitleSelectionListener.onTitleSelection(position);
					}
				}
			});
		}
		setSelect(0);
		postInvalidate();
	}
	
	public void setSelect(int position) {
		this.mPosition = position;
		for (int index = 0; index < mTitleCount; index++) {
			mTvTitles[index].setSelected(index == position);
		}
		mTranslationX = position * mTitleWidth;
	}
	
	public int getSelect() {
		return mPosition;
	}
	
	public void scroll(int position, float offset) {
		mTranslationX = position * mTitleWidth + (int) (mTitleWidth * offset);
		postInvalidate();
	}
	
	public void showNotice(int position, boolean show) {
		mIvTitles[position].setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void setViewPager(ViewPager viewPager) {
		mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}
	
	public void setOnTitleSelectionListener(OnTitleSelectionListener listener) {
		this.mTitleSelectionListener = listener;
	}
	
	public static interface OnTitleSelectionListener {
		public void onTitleSelection(int position);
	}
	
}
