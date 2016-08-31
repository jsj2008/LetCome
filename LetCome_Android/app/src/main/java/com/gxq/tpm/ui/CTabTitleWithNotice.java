package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;
import com.gxq.tpm.tools.PageChangeListener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CTabTitleWithNotice extends CTabTitleSelector implements View.OnClickListener {
	private ViewPager mViewPager;
	
	private List<ImageView> mIvChildren;
	
	private OnPageChangeListener mPageChangeListener = new PageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setPosition(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}
	};
	
	public CTabTitleWithNotice(Context context) {
		this(context, null);
	}
	
	public CTabTitleWithNotice(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CTabTitleWithNotice(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mIvChildren = new ArrayList<ImageView>();
	}
	
	@Override
	protected void setTitle(View child, CharSequence text) {
		TextView tv = ((TextView) child.findViewById(R.id.tv_title));
		tv.setText(text);
		
		mIvChildren.add((ImageView) child.findViewById(R.id.iv_title));
	}

	@Override
	protected int getLayoutId() {
		return R.layout.tab_title;
	}
	
	@Override
	public int getSeparateLayoutId() {
		return R.layout.tab_title_vertical_sep;
	}
	
	public void showNotice(int position, boolean show) {
		mIvChildren.get(position).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void setViewPager(ViewPager viewPager) {
		mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}
	
}
