package com.gxq.tpm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.letcome.R;

public class CTabIconBarItem extends CTabBarItem {

	public CTabIconBarItem(Context context) {
		this(context, null);
	}
	public CTabIconBarItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CTabIconBarItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		View.inflate(context, R.layout.tab_icon_bar_item, this);
		mIvTab = (ImageView) findViewById(R.id.iv_tab);
		mIvRemind = (ImageView) findViewById(R.id.iv_remind);
	}



}
