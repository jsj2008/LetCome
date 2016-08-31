package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CTabTitleWithScrollbar extends CTabTitleSelector implements View.OnClickListener {
	private int mTitleWidth;
	
	private int mTranslationX;
	private int mTitleBarHeight;
	private Paint mPaint = new Paint();
	
	private List<ImageView> mIvChildren;
	
	public CTabTitleWithScrollbar(Context context) {
		this(context, null);
	}
	
	public CTabTitleWithScrollbar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CTabTitleWithScrollbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mPaint.setColor(Util.transformColor(R.color.tab_cur_selected));
		mTitleBarHeight = Util.transformDimen(R.dimen.margin_xhdpi_2);
		
		mIvChildren = new ArrayList<ImageView>();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mTitleWidth = w / mChildren.size();
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
	
	@Override
	protected void setTitle(View child, CharSequence text) {
		((TextView) child.findViewById(R.id.tv_title)).setText(text);
		
		mIvChildren.add((ImageView) child.findViewById(R.id.iv_title));
	}

	@Override
	protected int getLayoutId() {
		return R.layout.tab_title;
	}
	
	@Override
	public void setPosition(int position) {
		if (position < mChildren.size()) {
			super.setPosition(position);
			
			mTranslationX = position * mTitleWidth;
		}
	}
	
	@Override
	protected void scroll(int position, float offset) {
		mTranslationX = position * mTitleWidth + (int) (mTitleWidth * offset);
		postInvalidate();
	}
	
	public void showNotice(int position, boolean show) {
		mIvChildren.get(position).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}
	
}
