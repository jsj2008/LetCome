package com.gxq.tpm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.letcome.R;

public class CTabFullBarItem extends CTabBarItem {
	protected TextView mTvTab;

	public CTabFullBarItem(Context context) {
		this(context, null);
	}
	public CTabFullBarItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CTabFullBarItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		View.inflate(context, R.layout.tab_bar_item, this);
		mIvTab = (ImageView) findViewById(R.id.iv_tab);
		mIvRemind = (ImageView) findViewById(R.id.iv_remind);
		mTvTab = (TextView) findViewById(R.id.tv_tab);
	}

	public void setText(int resId) {
		mTvTab.setText(resId);
	}
}
