package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CPointProgressBar extends LinearLayout {
	private int mMax;
	
	private List<View> mChildren;
	
	public CPointProgressBar(Context context) {
		this(context, null);
	}

	public CPointProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CPointProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setOrientation(HORIZONTAL);
		mChildren = new ArrayList<View>();
	}

	public void setMax(int max) {
		if (mMax == max) {
			return;
		}

		if (mMax < max) {
			int dimension = getResources().getDimensionPixelOffset(R.dimen.margin_xhdpi_5);
			for (int i = mMax; i < max; i++) {
				View view = new View(getContext());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dimension, dimension);
				
				if (i != 0) {
					lp.leftMargin = dimension;
				}
				
				addView(view, lp);
				mChildren.add(view);
			}
		} else {
			for (int i = max; i < mMax; i++) {
				View child = mChildren.remove(i);
				removeView(child);
			}
		}
		mMax = max;
		mChildren.get(mMax - 1).setBackgroundResource(R.drawable.progressbar_point_last);
		setProgress(0);
	}
	
	public void setProgress(int progress) {
		for (int i = 0; i < mMax - 1; i++) {
			if (i < progress) {
				mChildren.get(i).setBackgroundResource(R.drawable.progressbar_point_progress);
			} else {
				mChildren.get(i).setBackgroundResource(R.drawable.progressbar_point_bg);
			}
		}
	}
	
}
