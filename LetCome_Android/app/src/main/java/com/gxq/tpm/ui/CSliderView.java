package com.gxq.tpm.ui;

import com.letcome.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CSliderView extends LinearLayout implements View.OnClickListener {
	private int mDrawableId;
	private int mItemNum;
	private int mSelected;
	
	private View[] mChildrenView = new View[0];
	
	private OnSliderViewSelectListener mListener;
	
	public CSliderView(Context context) {
		this(context, null);
	}
	
	public CSliderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CSliderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setOrientation(HORIZONTAL);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CSliderView);
		
		mItemNum = a.getInt(R.styleable.CSliderView_item_num, 0);
		mDrawableId = a.getResourceId(R.styleable.CSliderView_src, 0);
		mSelected = a.getInteger(R.styleable.CSliderView_selected, 0);
		
		a.recycle();
		
		if (mItemNum > 0 && mDrawableId > 0) {
			setNum(mItemNum, mSelected);
		}
	}
	
	@Override
	public void onClick(View v) {
		for (int i = 0; i < mChildrenView.length; i++) {
			if (v == mChildrenView[i]) {
				if (mListener != null) {
					setSelect(i);
					mListener.onSliderViewSelect(i);
				}
				break;
			}
		}
	}
	
	public void setSliderDrawable(int id) {
		mDrawableId = id;
	}
	
	public void setNum(int num) {
		setNum(num, 0);
	}
	
	public void setNum(int num, int selected) {
		removeAllViews();
		
		mItemNum = num;
		mSelected = selected;
		
		mChildrenView = new ImageView[num];
		LayoutInflater inflater = LayoutInflater.from(getContext());
		for (int index = 0; index < num; index++) {
			ImageView iv = (ImageView) inflater.inflate(R.layout.slider_view_item, this, false);
			iv.setImageResource(mDrawableId);
			addView(iv);
			
			mChildrenView[index] = iv;
			mChildrenView[index].setOnClickListener(this);
		}
		setSelect(mSelected);
	}
	
	public void addChild(View[] children, int selected) {
		removeAllViews();
		
		mItemNum = children.length;
		mSelected = selected;
		
		mChildrenView = new View[mItemNum];
		for (int index = 0; index < mItemNum; index++) {
			addView(children[index]);
			mChildrenView[index] = children[index];
			mChildrenView[index].setOnClickListener(this);
		}
		setSelect(mSelected);
	}
	
	public void setSelect(int selected) {
		mSelected = selected;
		for (int index = 0; index < mChildrenView.length; index++) {
			mChildrenView[index].setSelected(index == selected);
		}
	}
	
	public void setOnSliderViewSelectListener(OnSliderViewSelectListener listener) {
		mListener = listener;
	}

	public static interface OnSliderViewSelectListener {
		public void onSliderViewSelect(int position);
	}
	
}
