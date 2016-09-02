package com.gxq.tpm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CTabBarItem extends LinearLayout {
	protected ImageView mIvTab, mIvRemind;
	
	public CTabBarItem(Context context) {
		this(context, null);
	}
	public CTabBarItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CTabBarItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImageResource(int resId) {
		mIvTab.setImageResource(resId);
	}
	
	public void setRemind(boolean remind) {
		mIvRemind.setVisibility(remind ? View.VISIBLE : View.INVISIBLE);
	}



}
