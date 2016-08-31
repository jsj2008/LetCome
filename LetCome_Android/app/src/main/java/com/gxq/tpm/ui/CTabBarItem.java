package com.gxq.tpm.ui;

import com.letcome.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CTabBarItem extends LinearLayout {
	private ImageView mIvTab, mIvRemind;
	private TextView mTvTab;
	
	public CTabBarItem(Context context) {
		this(context, null);
	}
	public CTabBarItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CTabBarItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		View.inflate(context, R.layout.tab_bar_item, this);
		mIvTab = (ImageView) findViewById(R.id.iv_tab);
		mIvRemind = (ImageView) findViewById(R.id.iv_remind);
		mTvTab = (TextView) findViewById(R.id.tv_tab);
	}
	
	public void setImageResource(int resId) {
		mIvTab.setImageResource(resId);
	}
	
	public void setRemind(boolean remind) {
		mIvRemind.setVisibility(remind ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void setText(int resId) {
		mTvTab.setText(resId);
	}

}
