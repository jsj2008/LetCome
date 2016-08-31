package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.gxq.tpm.tools.PageChangeListener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CTabTitleSelector extends LinearLayout implements View.OnClickListener {

	protected List<View> mChildren;
	protected int mPosition = -1;
	
	private int mLayoutId;
	private int mSeparateLayoutId;
	private int mSeparate;
	private ViewPager mViewPager;
	
	protected OnTabTitleSelectListener mTabTitleSelectListener;
	private OnPageChangeListener mPageChangeListener = new PageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setPosition(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			scroll(position, positionOffset);
		}
	};
	
	public CTabTitleSelector(Context context) {
		this(context, null);
	}
	
	public CTabTitleSelector(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CTabTitleSelector(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mChildren = new ArrayList<View>();
		setOrientation(HORIZONTAL);
	}
	
	public void newTabTitle(int resId) {
		newTabTitle(getResources().getString(resId));
	}

	public void newTabTitle(CharSequence text) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		
		if (mChildren.size() > 0) {
			if (getSeparateLayoutId() != 0) {
				View.inflate(getContext(), getSeparateLayoutId(), this);
			} else if (mSeparate > 0) {
				View sepView = new View(getContext());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						mSeparate, LinearLayout.LayoutParams.MATCH_PARENT);
				addView(sepView, lp);
			}
		}
		View child = (View) inflater.inflate(getLayoutId(), this, false);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 
				LinearLayout.LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		child.setLayoutParams(lp);
		mChildren.add(child);
		
		addView(child);
		
		setTitle(child, text);
		child.setOnClickListener(this);
	}
	
	protected void setTitle(View child, CharSequence text) {
		if (child instanceof TextView) {
			((TextView)child).setText(text);
		}
	}

	protected int getLayoutId() {
		return mLayoutId;
	}
	
	public void setLayoutId(int layoutId) {
		this.mLayoutId = layoutId;
	}
	
	protected int getSeparateLayoutId() {
		return mSeparateLayoutId;
	}
	
	public void setSeparateLayoutId(int layoutId) {
		this.mSeparateLayoutId = layoutId;
	}
	
	public void setSeparate(int sep) {
		this.mSeparate = sep;
	}
	
	public void setPosition(int position) {
		if (position < mChildren.size()) {
			mPosition = position;
			
			for (int index = 0; index < mChildren.size(); index++) {
				mChildren.get(index).setSelected(mPosition == index);
			}
		}
	}
	
	public void setEnable(int position, boolean enable) {
		if (position < mChildren.size()) {
			mChildren.get(position).setEnabled(enable);
		}
	}
	
	@Override
	public void onClick(View v) {
		for (int index = 0; index < mChildren.size(); index++) {
			if (mChildren.get(index) == v) {
				if (index == mPosition) break;
				
				setPosition(index);
				
				if (mTabTitleSelectListener != null) {
					mTabTitleSelectListener.onSelection(index);
				}
				break;
			}
		}
	}
	
	protected void scroll(int position, float offset) {
	}
	
	public void setViewPager(ViewPager viewPager) {
		mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}
	
	public void setOnTabTitleSelectListener(OnTabTitleSelectListener mListener) {
		this.mTabTitleSelectListener = mListener;
	}
	
	public static interface OnTabTitleSelectListener {
		public void onSelection(int position);
	}
	
}
